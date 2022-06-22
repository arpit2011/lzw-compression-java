/**
 * Name: Arpit Agrawal
 * Andrew ID: arpitagr
 * Course: Data structures and algorithms
 * Project 5
 */
package lzw;


import java.io.*;

/**
 * Class to perform the lzw compression and decompression
 */
public class LZWCompression {
    /**
     * Method to compress the given file
     * @param inputFile
     * Name of input filename
     * @param ouputFile
     * Name of output filename
     * @throws IOException
     */
    public void LZW_Compress(String inputFile, String ouputFile) throws IOException {
        System.out.println("Compressing..please wait..");
        //Populating the map with initial 255 ASCII values
        HashMap<String, Integer> hashMap = resestDictionary();
        //Buffer array for writing in 12 bits patter
        byte[] buffer = new byte[3];
        boolean write = true;
        //Input and output files
        DataInputStream in = readFile(inputFile);
        DataOutputStream out = getOutputFile(ouputFile);
        String s = "";
        try {
            //First byte
            byte b1 = in.readByte();
            //remove the extra bits
            char c = (char) (b1 & 0xFF);
            s = c + "";
            while (true) {
                //Second byte
                byte b2 = in.readByte();
                c = (char) (b2 & 0xFF);
                if (hashMap.containsKey(s + c)) {
                    //Adding to the string
                    s += c;
                } else {
                    //Writing to the output file
                    buffer = writeToComFile(write, buffer, hashMap.get(s), out, false);
                    //Handling map overflow
                    if (hashMap.size() < 4096) {
                        hashMap.put(s + c, hashMap.size());
                    } else {
                        hashMap = resestDictionary();
                        hashMap.put(s + c, hashMap.size());
                    }
                    write = !write;
                    s = c + "";
                }
            }
        } catch (Exception e) {
            //Writing the last byte
            writeToComFile(write, buffer, hashMap.get(s), out, true);
            System.out.println("Compression done!");
        } finally {
            in.close();
            out.close();
        }
    }

    /**
     * Helper method to write the compressed data
     * @param write
     * indicator for 3 byte completion
     * @param buffer
     * 3 bytes
     * @param value
     * value to be written
     * @param out
     * Out object
     * @param finalByte
     * last byte
     * @return
     * updated buffer array
     * @throws IOException
     */
    public byte[] writeToComFile(boolean write, byte[] buffer, int value, DataOutputStream out, boolean finalByte) throws IOException {
        if (write) {
            //https://stackoverflow.com/questions/6996707/java-byte-parsebyte-error
            //buffer[0] = Byte.parseByte(toWrite.substring(0,8));
            //String temp = toWrite.substring(8, 12) + "0000";
            //buffer[1] = Byte.parseByte(temp,2);

            //Unsigned Right shift for left zeros
            buffer[0] = (byte) (value >>> 4);
            //Left shift for right zeros
            buffer[1] = (byte) (value << 4);
            //if last byte, write 16 bits of data with 4 0 padding
            if (finalByte) {
                for (int k = 0; k < 2; k++) {
                    out.writeByte(buffer[k]);
                }
            }
        } else {
            buffer[1] = (byte) (buffer[1] | (value >>> 8));
            buffer[2] = (byte) (value);
            //writing the 3 bytes
            for (byte b : buffer) {
                out.writeByte(b);
            }
            buffer = new byte[3];
        }
        return buffer;
    }

    /**
     * Method to decompress the given file
     * @param inputFile
     * Name of input filename
     * @param ouputFile
     * Name of output filename
     * @throws IOException
     */
    public void LZW_Decompress(String inputFile, String ouputFile) throws IOException {
        System.out.println("Decompressing..please wait..");
        //Initialing normal array
        String[] decomArray = resetDecompressArray() ;
        //files
        DataInputStream in = readFile(inputFile);
        DataOutputStream out = getOutputFile(ouputFile);
        //Buffer array to write data
        byte[] buffer = new byte[3];
        //initial size of the array
        int i = 256;
        boolean write = false;
        int prior;
        int next;
        try {
            //Reading first 2 bytes
            buffer[0] = in.readByte();
            buffer[1] = in.readByte();
            //Getting int value
            prior = getIntValue(true, buffer, true);
            //Getting the ascii from int value
            String temp = decomArray[prior];
            //Writing the string to the file
            out.writeBytes(temp);
            while(true){
                //reading further bytes. write flag to get the 12 bit chunk and convert into 8 bits
                if(write){
                    buffer[0] = in.readByte();
                    buffer[1] = in.readByte();
                    next = getIntValue(write,buffer, true);
                }else{
                    buffer[2] = in.readByte();
                    next = getIntValue(write,buffer, false);
                }
                write = !write;
                //checking if the value exists in the array
                if(decomArray[next] == null){
                    String val = decomArray[prior] + (char) (decomArray[prior].charAt(0) & 0xFF);
                    if(i == 4096){
                        i = 256;
                        decomArray = resetDecompressArray();
                    }
                    decomArray[i] = val;
                    out.writeBytes(decomArray[i]);
                    i++;
                }else{
                    String val = decomArray[prior] + (char) (decomArray[next].charAt(0) & 0xFF);
                    if(i == 4096){
                        i = 256;
                        decomArray = resetDecompressArray();
                    }
                    decomArray[i] = val;
                    out.writeBytes(decomArray[next]);
                    i++;
                }
                prior = next;
            }

        } catch (EOFException e) {
            System.out.println("Decompression done");
        } finally {
            in.close();
            out.close();
        }
    }

    /**
     * Helper class to get the Int value of bytes
     * @param write
     * write flag
     * @param buffer
     * byte buffer
     * @param first
     * first byte indicator
     * @return
     * int value
     * @throws IOException
     */
    public int getIntValue(boolean write, byte[] buffer, boolean first) throws IOException {
        int b1,b2;
        //getting the bytes in int
        if(first) {
            b1 = buffer[0] & 0xFF;
            b2 = buffer[1] & 0xFF;
        }else{
            b1 = buffer[1] & 0xFF;
            b2 = buffer[2] & 0xFF;
        }
        int value;
        //Shifting the bytes to get the original values
        if(write){
            value = (b1 << 4) | (b2 >>> 4);
        }else{
            value = ((b1 & 0x0F) << 8) | b2;
        }
        return value;
    }

    /**
     * Reseting the map to initial value
     * @return
     * Maps
     */
    public HashMap resestDictionary() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            String ch = (char) i + "";
            hashMap.put(ch, i);
        }
        return hashMap;
    }

    /**
     * reseting the String array to initial values
     * @return
     * Array
     */
    public String[] resetDecompressArray(){
        String[] strArray = new String[4096];
        for(int i = 0; i < 256; i++){
            String ch = (char) i + "";
            strArray[i] = ch;
        }
        return strArray;
    }

    /**
     * Reads the input file
     * @param fileName
     * file name
     * @return
     * DataInputStream obj
     */
    public DataInputStream readFile(String fileName) {
        DataInputStream in = null;
        try {
            in = new DataInputStream(
                    new BufferedInputStream(
                            new FileInputStream(fileName)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }

    /**
     * Writes the output file
     * @param fileName
     * output filename
     * @return
     * DataOutputStream obj
     */
    public DataOutputStream getOutputFile(String fileName) {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * Prints the byte details
     * @param inputFile
     * input file name
     * @param ouputFile
     * output file name
     */
    private void printByteDetails(String inputFile, String ouputFile){
        File inFile = new File(inputFile);
        File outFile = new File(ouputFile);
        System.out.println("bytes read = " + inFile.length() +", bytes written = " + outFile.length());
    }

    /**
     * Main class - driver
     * This is LZW 12 bit compression algorithm. The file is read byte by byte and these bytes are converted into 12
     * bits. This is done using a hashmap. The hashmap is initialized with 256 ASCII characters. Everytime the byte is
     * read, its ASCII value is checked in the hashmap. If the value is present, its written to the output file else
     * next byte is appended the ASCII value to these 2 bytes are checked with the table. This continues till the
     * whole file is read. The input file could be ASCII file or binary files.
     *
     * Decompression is done using the reverse technique. 12 bit chunks are read and its decoded in 8 bits and
     * corresponding ASCII character is stored in the output file. Overall the compression reduces the size of the file
     * significantly. But of the binary files(video file), the size of the file increased. This is because the binary
     * files have the lowest degree of compression since binary files have the lowest redundancy.
     *
     * Degree of compression:
     * 1. words.html: bytes read = 2493531, bytes written = 1069779 --- 57.0978% compression
     * 2. CrimeLatLonXY1990.csv: bytes read = 2608940, bytes written = 1283664 --- 50.7975% compression
     * 3. 01_Overview.mp4: bytes read = 25008838, bytes written = 33773081 ---  -35.0446% compression
     *
     * @param args
     * args array
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        LZWCompression lzw = new LZWCompression();
        //if -v is in args
        if (args.length == 4) {
            if (args[0].equals("-c")) {
                lzw.LZW_Compress(args[2], args[3]);
                if(args[1].equals("-v")){
                    lzw.printByteDetails(args[2], args[3]);
                }else{
                    throw new IllegalArgumentException("Invalid arguments");
                }
            } else if (args[0].equals("-d")) {
                lzw.LZW_Decompress(args[2], args[3]);
                if(args[1].equals("-v")){
                    lzw.printByteDetails(args[2], args[3]);
                }else{
                    throw new IllegalArgumentException("Invalid arguments");
                }
            } else {
                throw new IllegalArgumentException("Invalid arguments");
            }
        } else if (args.length == 3) {
            if (args[0].equals("-c")) {
                lzw.LZW_Compress(args[1], args[2]);
            } else if (args[0].equals("-d")) {
                lzw.LZW_Decompress(args[1], args[2]);
            } else {
                throw new IllegalArgumentException("Invalid arguments");
            }
        } else {
            throw new IllegalArgumentException("Invalid arguments");
        }
//        HashMap<String, Integer> hashMap = new HashMap<>();
//        for(int i = 65; i < 97; i++){
//            hashMap.put("" + (char)i, i);
//        }
//        hashMap.put("A", 108);
//        System.out.println(hashMap.get("A"));
//        System.out.println(hashMap.contains("C"));
//        System.out.println(hashMap.contains("ASB"));
//        hashMap.put("ASB", 19383);
//        hashMap.put("AC6", 18283);
//        System.out.println(hashMap.contains("ASB"));
//        hashMap.remove("AC6");
    }
}
