// File: ObjectNode.java from the package edu.colorado.nodes
// Complete documentation is available from the ObjectNode link in:
//   http://www.cs.colorado.edu/~main/docs
/**
 * Name: Arpit Agrawal
 * Andrew ID: arpitagr
 * Course: Data structures and algorithms
 * Project 5
 */
package lzw;

/******************************************************************************
 * A ObjectNode provides a node for a linked list with
 * Object data in each node.
 *
 * @note
 *   Lists of nodes can be made of any length, limited only by the amount of
 *   free memory in the heap. But beyond Integer.MAX_VALUE (2,147,483,647),
 *   the answer from listLength is incorrect because of arithmetic
 *   overflow.
 *
 * @see
 *   <A HREF="../../../../edu/colorado/nodes/ObjectNode.java">
 *   Java Source Code for this class
 *   (www.cs.colorado.edu/~main/edu/colorado/nodes/ObjectNode.java) </A>
 *
 * @author Michael Main
 *   <A HREF="mailto:main@colorado.edu"> (main@colorado.edu) </A>
 *
 * @version Feb 10, 2016
 ******************************************************************************/
public class ObjectNode<T> {
    // Invariant of the ObjectNode class:
    //   1. The node's Object data is in the instance variable data.
    //   2. For the final node of a list, the link part is null.
    //      Otherwise, the link part is a reference to the
    //      next node of the list.
    private T data;
    private ObjectNode link;


    /**
     * Initialize a node with a specified initial data and link to the next
     * node. Note that the initialLink may be the null reference,
     * which indicates that the new node has nothing after it.
     *
     * @param initialData the initial data of this new node
     * @param initialLink a reference to the node after this new node--this reference may be null
     *                    to indicate that there is no node after this new node.
     * @postcondition This node contains the specified data and link to the next node.
     **/
    public ObjectNode(T initialData, ObjectNode initialLink) {
        data = initialData;
        link = initialLink;
    }

    public ObjectNode() {
        data = null;
        link = null;
    }

    /**
     * Copy a list.
     *
     * @param source the head of a linked list that will be copied (which may be
     *               an empty list in where source is null)
     * @return The method has made a copy of the linked list starting at
     * source. The return value is the head reference for the
     * copy.
     * @throws OutOfMemoryError Indicates that there is insufficient memory for the new list.
     *                          <p>
     *                          θ(N)
     **/
    public static ObjectNode listCopy(ObjectNode source) {
        ObjectNode copyHead;
        ObjectNode copyTail;

        // Handle the special case of the empty list.
        if (source == null)
            return null;

        // Make the first node for the newly created list.
        copyHead = new ObjectNode(source.data, null);
        copyTail = copyHead;

        // Make the rest of the nodes for the newly created list.
        while (source.link != null) {
            source = source.link;
            //link = new ObjectNode(item, link);
            copyTail.addNodeAfter(source.data);
            copyTail = copyTail.link;
        }

        // Return the head reference for the new list.
        return copyHead;
    }

    /**
     * @param source the head of a linked list that will be copied (which may be
     *               an empty list in where source is null)
     * @return The method has made a copy of the linked list starting at
     * source. The return value is the head reference for the
     * copy.
     * θ(N)
     */
    public static ObjectNode listCopy_rec(ObjectNode source) {
        ObjectNode copyHead;
        if (source == null) {
            return null;
        } else {
            ObjectNode node;
            node = listCopy_rec(source.link);
            copyHead = new ObjectNode(source.data, node);
            //   copyHead.addNodeAfter(listCopy_rec(source.link));
            return copyHead;
        }
    }

    /**
     * Copy a list, returning both a head and tail reference for the copy.
     *
     * @param source the head of a linked list that will be copied (which may be
     *               an empty list in where source is null)
     * @return The method has made a copy of the linked list starting at
     * source.  The return value is an
     * array where the [0] element is a head reference for the copy and the [1]
     * element is a tail reference for the copy.
     * @throws OutOfMemoryError Indicates that there is insufficient memory for the new list.
     *                          θ(N)
     **/
    public static ObjectNode[] listCopyWithTail(ObjectNode source) {
        ObjectNode copyHead;
        ObjectNode copyTail;
        ObjectNode[] answer = new ObjectNode[2];

        // Handle the special case of the empty list.
        if (source == null)
            return answer; // The answer has two null references .

        // Make the first node for the newly created list.
        copyHead = new ObjectNode(source.data, null);
        copyTail = copyHead;

        // Make the rest of the nodes for the newly created list.
        while (source.link != null) {
            source = source.link;
            copyTail.addNodeAfter(source.data);
            copyTail = copyTail.link;
        }

        // Return the head and tail references.
        answer[0] = copyHead;
        answer[1] = copyTail;
        return answer;
    }

    /**
     * Compute the number of nodes in a linked list.
     *
     * @param head the head reference for a linked list (which may be an empty list
     *             with a null head)
     * @return the number of nodes in the list with the given head
     * @precondition List should not be a circular list i.e. end node should point to null
     * @note A wrong answer occurs for lists longer than Int.MAX_VALUE.
     * θ(N)
     **/
    public static int listLength(ObjectNode head) {
        ObjectNode cursor;
        int answer;

        answer = 0;
        for (cursor = head; cursor != null; cursor = cursor.link)
            answer++;

        return answer;
    }

    /**
     * * @precondition
     * * List should not be a circular list i.e. end node should point to null
     *
     * @param head the head reference for a linked list (which may be an empty list
     *             with a null head)
     * @return the number of nodes in the list with the given head
     * θ(N)
     */
    public static int listLength_rec(ObjectNode head) {
        if (head == null) {
            return 0;
        } else {
            return 1 + listLength_rec(head.link);
        }
    }


    /**
     * Copy part of a list, providing a head and tail reference for the new copy.
     *
     * @param start first node to copy
     * @param end   final node to copy
     * @return The method has made a copy of the part of a linked list, from the
     * specified start node to the specified end node. The return value is an
     * array where the [0] component is a head reference for the copy and the
     * [1] component is a tail reference for the copy.
     * @throws IllegalArgumentException Indicates that start and end are not references
     *                                  to nodes on the same list.
     * @throws NullPointerException     Indicates that start is null.
     * @throws OutOfMemoryError         Indicates that there is insufficient memory for the new list.
     *                                  θ(N)
     * @precondition start and end are non-null references to nodes
     * on the same linked list,
     * with the start node at or before the end node.
     **/
    public static ObjectNode[] listPart(ObjectNode start, ObjectNode end) {
        ObjectNode copyHead;
        ObjectNode copyTail;
        ObjectNode cursor;
        ObjectNode[] answer = new ObjectNode[2];

        // Make the first node for the newly created list. Notice that this will
        // cause a NullPointerException if start is null.
        copyHead = new ObjectNode(start.data, null);
        copyTail = copyHead;
        cursor = start;

        // Make the rest of the nodes for the newly created list.
        while (cursor != end) {
            cursor = cursor.link;
            if (cursor == null)
                throw new IllegalArgumentException
                        ("end node was not found on the list");
            copyTail.addNodeAfter(cursor.data);
            copyTail = copyTail.link;
        }

        // Return the head and tail references
        answer[0] = copyHead;
        answer[1] = copyTail;
        return answer;
    }

    /**
     * Find a node at a specified position in a linked list.
     *
     * @param head     the head reference for a linked list (which may be an empty list in
     *                 which case the head is null)
     * @param position a node number
     * @return The return value is a reference to the node at the specified position in
     * the list. (The head node is position 1, the next node is position 2, and
     * so on.) If there is no such position (because the list is too short),
     * then the null reference is returned.
     * @throws IllegalArgumentException Indicates that position is not positive.
     *                                  θ(N)
     * @precondition position &gt; 0.
     **/
    public static ObjectNode listPosition(ObjectNode head, int position) {
        ObjectNode cursor;
        int i;

        if (position <= 0)
            throw new IllegalArgumentException("position is not positive");

        cursor = head;
        for (i = 1; (i < position) && (cursor != null); i++)
            cursor = cursor.link;

        return cursor;
    }

    /**
     * removes the object at given position
     * @param position
     * position
     */
    public void remove(int position){
        ObjectNode cursor;
        int i;
        if (position <= 0)
            throw new IllegalArgumentException("position is not positive");
        if(position == 1){
            data = (T) link.data;
            link = link.link;
        }else {
            cursor = this;
            for(i = 1;(i < (position-1)) && (cursor != null); i++){
                cursor = cursor.link;
            }
            if (cursor == null || cursor.link == null){
                return;
            }
            ObjectNode next = cursor.link.link;
            cursor.link = next;
        }
    }
    /**
     * Search for a particular piece of data in a linked list.
     *
     * @param head   the head reference for a linked list (which may be an empty list in
     *               which case the head is null)
     * @param target a piece of data to search for
     * @return The return value is a reference to the first node that contains the
     * specified target. If there is no such node, the null reference is
     * returned.
     * θ(N)
     **/
    public static ObjectNode listSearch(ObjectNode head, Object target) {
        ObjectNode cursor;

        for (cursor = head; cursor != null; cursor = cursor.link)
            if (target == cursor.data)
                return cursor;

        return null;
    }

    /**
     * Modification method to add a new node after this node.
     *
     * @param item the data to place in the new node
     * @throws OutOfMemoryError Indicates that there is insufficient memory for a new
     *                          ObjectNode.
     *                          θ(1)
     * @postcondition A new node has been created and placed after this node.
     * The data for the new node is item. Any other nodes
     * that used to be after this node are now after the new node.
     **/
    public void addNodeAfter(T item) {
        link = new ObjectNode(item, link);
    }

    /**
     * Accessor method to get the data from this node.
     *
     * @return the data from this node
     * θ(1)
     **/
    public T getData() {
        return data;
    }

    /**
     * Modification method to set the data in this node.
     *
     * @param newData the new data to place in this node
     * @postcondition The data of this node has been set to newData.
     * θ(1)
     **/
    public void setData(T newData) {
        data = newData;
    }

    /**
     * Accessor method to get a reference to the next node after this node.
     *
     * @return a reference to the node after this node (or the null reference if there
     * is nothing after this node)
     * <p>
     * θ(1)
     **/
    public ObjectNode getLink() {
        return link;
    }

    /**
     * Modification method to set the link to the next node after this node.
     *
     * @param newLink a reference to the node that should appear after this node in the linked
     *                list (or the null reference if there is no node after this node)
     * @postcondition The link to the node after this node has been set to newLink.
     * Any other node (that used to be in this link) is no longer connected to
     * this node.
     * θ(1)
     **/
    public void setLink(ObjectNode newLink) {
        link = newLink;
    }

    /**
     * Modification method to remove the node after this node.
     *
     * @throws NullPointerException Indicates that this was the tail node of the list, so there is nothing
     *                              after it to remove.
     *                              θ(1)
     * @precondition This node must not be the tail node of the list.
     * @postcondition The node after this node has been removed from the linked list.
     * If there were further nodes after that one, they are still
     * present on the list.
     **/
    public void removeNodeAfter() {
        link = link.link;
    }

    /**
     * @param source the head of a linked list which may be null
     *               θ(N)
     */
    public static void displayEveryThird(ObjectNode source) {
        if (source != null) {
            int listLenght = ObjectNode.listLength(source);
            for (int i = 1; i <= listLenght; i++) {
                if (i % 3 == 0) {
                    System.out.print(ObjectNode.listPosition(source, i).data);
                }
            }
        } else {
            System.out.println("No data in the list");
        }
    }

    /**
     * @return data of the linked list in a String
     * θ(N)
     */
    @Override
    public String toString() {
        String listData;
        listData = data + "";
        ObjectNode next;
        next = link;
        while (next != null) {
            listData += next.data;
            next = next.link;
        }
        return listData;
    }

    /**
     * Driver
     *
     * @param args θ(N)
     */
    public static void main(String[] args) {
        String list = "abcdefghijklmnopqrstuvwxyz";
        ObjectNode headNode, tailNode;
        // Initialize the list and add the first data
        headNode = new ObjectNode(list.charAt(0), null);
        tailNode = headNode;
//      solution 1
        for (int i = 1; i < list.length(); i++) {
            tailNode.addNodeAfter(list.charAt(i));
            tailNode = tailNode.link;
        }
        //Printing the list
        System.out.println(headNode.toString());
        //Displaying every 3rd node of the list
        ObjectNode.displayEveryThird(headNode);
        System.out.println();
        //Displaying the length of the list
        System.out.println("Number of nodes = " + ObjectNode.listLength(headNode));
        System.out.println("Number of nodes = " + ObjectNode.listLength_rec(headNode));
        //Copying the list into a new object k
        ObjectNode k = ObjectNode.listCopy(headNode);
        System.out.println(k.toString());
        System.out.println("Number of nodes in K= " + k.listLength(headNode));
        System.out.println("Number of nodes in K= " + k.listLength_rec(headNode));
        //copying using the recursive method
        ObjectNode k2 = ObjectNode.listCopy_rec(headNode);
        System.out.println(k2.toString());
        System.out.println("Number of nodes in K2= " + k2.listLength(headNode));
        System.out.println("Number of nodes in K2= " + k2.listLength_rec(headNode));
    }
}
           