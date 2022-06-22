package lzw;
/**
 * Name: Arpit Agrawal
 * Andrew ID: arpitagr
 * Course: Data structures and algorithms
 * Project 5
 */
import java.util.Objects;

/**
 * https://medium.com/swlh/hashmap-implementation-for-java-90a5f58d4a5b
 * Class representing the hashmap. This is done using the array of linked list
 * @param <K> key datatype
 * @param <V> value datatype
 */
public class HashMap<K, V> {
    //size of the map
    private int size = 0;
    //Array of linkedlist
    private ObjectNode<MapEntry<K, V>>[] hashMap;

    /**
     * Constructor. Size 127 as per the project instructions
     */
    public HashMap() {
        hashMap = new ObjectNode[127];
    }

    /**
     * Computes the index by calculating modulus of key hascode and length of hashmap
     * @param key
     * key
     * @return
     * index
     */
    private int getHash(K key) {
        return Math.abs(key.hashCode()) % hashMap.length;
    }

    /**
     * Puts the new key value pair in the hashmap
     * @param key
     * key
     * @param value
     * value
     */
    public void put(K key, V value) {
//        if (size >= hashMap.length) {
//            increaseSize();
//        }
        //Checks if the key is already present in the map
        if (containsKey(key)) {
            MapEntry<K, V> entry = getMapEntry(key);
            //if present, override the value
            entry.setValue(value);
        } else {
            //get index
            int index = getHash(key);
            if (hashMap[index] == null) {
                hashMap[index] = new ObjectNode<>();
            }
            //Add value
            if(hashMap[index].getData() == null){
                hashMap[index].setData(new MapEntry(key, value));
            }else {
                ObjectNode<MapEntry<K, V>> entry = hashMap[index];
                ObjectNode<MapEntry<K, V>> temp = null;
                for (int i = 1; i <= ObjectNode.listLength(entry); i++) {
                    temp =  ObjectNode.listPosition(entry, i);
                }
                temp.addNodeAfter(new MapEntry(key, value));
            }
            size++;
        }
    }

    /**
     * method to increase the size of the hashmap. Not used in this project
     */
    private void increaseSize() {
        ObjectNode<MapEntry<K, V>>[] oldHashMap = hashMap;
        hashMap = new ObjectNode[size*2];
        for(int i = 0; i < oldHashMap.length; i++){
            if(oldHashMap[i] == null){
                continue;
            }else{
                hashMap[i] = oldHashMap[i];
            }
        }
    }

    /**
     * Get the MapEntry object by Key
     * @param key
     * key
     * @return
     * MapEntry
     */
    public MapEntry getMapEntry(K key) {
        int hash = getHash(key);
        ObjectNode<MapEntry<K, V>> entry = hashMap[hash];
        if (entry != null) {
            for (int i = 1; i <= ObjectNode.listLength(entry); i++) {
                MapEntry temp = (MapEntry) ObjectNode.listPosition(entry, i).getData();
                if (temp.getKey().equals(key)) {
                    return temp;
                }
            }
        }
        return null;
    }

    /**
     * Getter for value by key
     * @param key
     * key
     * @return
     * value
     */
    public V get(K key) {
        if (containsKey(key)) {
            return (V) getMapEntry(key).getValue();
        } else {
            return null;
        }
    }

    /**
     * Method check the if the key is present in the hashMap
     * @param key
     * key
     * @return
     * true if the key is present else false
     */
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        } else {
            return !Objects.isNull(getMapEntry(key));
        }
    }

    /**
     * Method to remove the entry from the map. Not used the project 5
     * @param key
     * key
     */
    public void remove(K key){
        if(containsKey(key)){
            int i = getHash(key);
            ObjectNode<MapEntry<K, V>> entry = hashMap[i];
            ObjectNode<MapEntry<K, V>> temp = null;
            for (int k = 1; k <= ObjectNode.listLength(entry); k++) {
                temp = ObjectNode.listPosition(entry, k);
                if(temp.getData().getKey().equals(key)){
                    entry.remove(k);
                    size--;
                    break;
                }

            }
        }
    }

    /**
     * getter for size
     * @return
     * size of the map
     */
    public int size(){
        return size;
    }
}
