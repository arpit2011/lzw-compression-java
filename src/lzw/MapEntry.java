/**
 * Name: Arpit Agrawal
 * Andrew ID: arpitagr
 * Course: Data structures and algorithms
 * Project 5
 */
package lzw;

/**
 * Class representing single entry of the map.
 * @param <K>
 * Key datatype
 * @param <V>
 *  Value dataType
 */
public class MapEntry<K, V> {
    //Key and Value pair
    private K key;
    private V value;

    /**
     * Constructor
     * @param key
     * key
     * @param value
     * value
     */
    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Default constructor
     */
    public MapEntry() {
    }

    /**
     * Getter for key
     * @return
     * key
     */
    public K getKey() {
        return key;
    }

    /**
     * Setter for key
     * @param key
     * key
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * getter for value
     * @return
     * value
     */
    public V getValue() {
        return value;
    }

    /**
     * Setter for value
     * @param value
     * value
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * To string key and value
     * @return
     * key and value in string format
     */
    @Override
    public String toString() {
        return key + ": " + value;
    }
}
