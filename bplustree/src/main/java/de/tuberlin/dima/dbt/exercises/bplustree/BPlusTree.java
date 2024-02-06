package de.tuberlin.dima.dbt.exercises.bplustree;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Implementation of a B+ tree.
 * <p>
 * The capacity of the tree is given by the capacity argument to the
 * constructor. Each node has at
 * least {capacity/2} and at most {capacity} many keys. The values are strings
 * and are stored at the
 * leaves of the tree.
 * <p>
 * For each inner node, the following conditions hold:
 * <p>
 * {pre} Integer[] keys = innerNode.getKeys(); Node[] children =
 * innerNode.getChildren(); {pre}
 * <p>
 * - All keys in {children[i].getKeys()} are smaller than {keys[i]}. - All keys
 * in
 * {children[j].getKeys()} are greater or equal than {keys[i]} if j > i.
 */
public class BPlusTree {

    ///// Implement these methods

    private LeafNode findLeafNode(Integer key, Node node, Deque<InnerNode> parents) {
        if (node instanceof LeafNode) {
            return (LeafNode) node;
        } else {
            InnerNode innerNode = (InnerNode) node;
            if (parents != null) {
                parents.push(innerNode);
            }
            // Done: traverse inner nodes to find leaf node
            Node[] children = innerNode.getChildren();
            int index = Arrays.binarySearch(node.keys, key);
            if (index < 0) {
                index = -(index + 1);
            } else if (node.keys[index] == key) {
                index++;
            }
            return findLeafNode(key, children[index], parents);
        }
    }

    private String lookupInLeafNode(Integer key, LeafNode node) {
        // Done: lookup value in leaf node
        int index = Arrays.binarySearch(node.keys, key);
        if (index >= 0) {
            return node.getValues()[index];
        }
        return null;
    }

    private <T> T[] insertArray(int pos, T value, T[] oldArray) {
        T[] newArray = Arrays.copyOf(oldArray, oldArray.length + 1);
        // int i = 0;
        // for (T t : oldArray) {
        // if (i == pos) {
        // newArray[i] = value;
        // i++;
        // }
        // newArray[i] = t;
        // i++;
        // }
        // return newArray;

        // Insert the new element
        newArray[pos] = value;

        // Copy elements after the insertion point
        System.arraycopy(oldArray, pos, newArray, pos + 1, oldArray.length - pos);

        return newArray;

    }

    private <T> T[] deleteArray(int pos, T[] oldArray) {
        T[] newArray = Arrays.copyOf(oldArray, oldArray.length - 1);
        // int i = 0;
        // for (T t : oldArray) {
        // if (i == pos) {
        // newArray[i] = value;
        // i++;
        // }
        // newArray[i] = t;
        // i++;
        // }
        // return newArray;

        // Copy elements after the insertion point
        System.arraycopy(oldArray, pos + 1, newArray, pos, newArray.length - pos);

        return newArray;

    }

    private <T> T[] concatWithArrayCopy(T[] array1, T[] array2) {
        T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    private void insertIntoInnerNode(Integer key, Node child, InnerNode node,
            Deque<InnerNode> parents) {
        // Insert in Inner
        // Find the index where the element should be inserted
        int insertIndex = Arrays.binarySearch(node.keys, key);

        // Adjust the index if the element
        // When present throw error
        if (insertIndex < 0) {
            insertIndex = -(insertIndex + 1);
        } else {
            // Key present: Tree dont need to change
            return;
        }

        Integer[] newKeys = insertArray(insertIndex, key, node.keys);
        Node[] newChildren = insertArray(insertIndex + 1, child, node.getChildren());
        // Check for capacity
        if (newKeys.length <= capacity) {
            // all good
            node.setKeys(newKeys);
            node.setChildren(newChildren);
            return;
        }

        // split inner node
        Integer[] keySplitOne = new Integer[(int) Math.floor(newKeys.length * 0.5)];
        Integer[] keySplitTwo = new Integer[(int) Math.floor(newKeys.length * 0.5)];
        Node[] valSplitOne = new Node[keySplitOne.length + 1];
        Node[] valSplitTwo = new Node[keySplitTwo.length + 1];
        int middleKey = newKeys[keySplitOne.length];

        System.arraycopy(newKeys, 0, keySplitOne, 0, keySplitOne.length);
        System.arraycopy(newKeys, keySplitOne.length + 1, keySplitTwo, 0, keySplitTwo.length);

        System.arraycopy(newChildren, 0, valSplitOne, 0, valSplitOne.length);
        System.arraycopy(newChildren, valSplitOne.length, valSplitTwo, 0, valSplitTwo.length);

        node.setKeys(keySplitOne);
        node.setChildren(valSplitOne);
        InnerNode newNode = new InnerNode(keySplitTwo, valSplitTwo, capacity);

        // was root
        if (parents.size() == 0) {
            Integer[] rKey = new Integer[1];
            Node[] rChilds = new Node[2];
            rKey[0] = middleKey;
            rChilds[0] = node;
            rChilds[1] = newNode;
            root = new InnerNode(rKey, rChilds, capacity);
        } else {
            insertIntoInnerNode(middleKey, newNode, parents.pop(), parents);
        }
    }

    private void insertIntoLeafNode(Integer key, String value, LeafNode node,
            Deque<InnerNode> parents) {
        // Done: insert value into leaf node (and propagate changes up)
        // Insert in leaf
        // Find the index where the element should be inserted
        int insertIndex = Arrays.binarySearch(node.keys, key);

        // Adjust the index if the element
        // When present throw error
        if (insertIndex < 0) {
            insertIndex = -(insertIndex + 1);
        } else {
            // Override value if key exists
            String[] values = node.getValues();
            values[insertIndex] = value;
            node.setValues(values);
            return;
        }
        Integer[] newKeys = insertArray(insertIndex, key, node.keys);
        String[] newValue = insertArray(insertIndex, value, node.getValues());
        // Check for capacity
        if (newKeys.length <= capacity) {
            // all good
            node.setKeys(newKeys);
            node.setValues(newValue);
            return;
        }

        // split leaf
        Integer[] keySplitOne = new Integer[(int) Math.floor(newKeys.length * 0.5)];
        Integer[] keySplitTwo = new Integer[(int) Math.ceil(newKeys.length * 0.5)];
        String[] valSplitOne = new String[keySplitOne.length];
        String[] valSplitTwo = new String[keySplitTwo.length];
        System.arraycopy(newKeys, 0, keySplitOne, 0, keySplitOne.length);
        System.arraycopy(newKeys, keySplitOne.length, keySplitTwo, 0, keySplitTwo.length);

        System.arraycopy(newValue, 0, valSplitOne, 0, valSplitOne.length);
        System.arraycopy(newValue, valSplitOne.length, valSplitTwo, 0, valSplitTwo.length);

        node.setKeys(keySplitOne);
        node.setValues(valSplitOne);
        LeafNode newNode = new LeafNode(keySplitTwo, valSplitTwo, capacity);

        // was root
        if (parents.size() == 0) {
            Integer[] rKey = new Integer[1];
            Node[] rChilds = new Node[2];
            rKey[0] = newNode.keys[0];
            rChilds[0] = node;
            rChilds[1] = newNode;
            root = new InnerNode(rKey, rChilds, capacity);
        } else {
            insertIntoInnerNode(newNode.keys[0], newNode, parents.pop(), parents);
        }

    }

    private void stealValue(LeafNode neighbor, LeafNode self, int stealPos, int insertIndex) {
        Integer[] leftKeys = deleteArray(stealPos, neighbor.keys);
        String[] leftValues = deleteArray(stealPos, neighbor.getValues());
        String stealValue = neighbor.getValues()[stealPos];
        int stealKey = neighbor.keys[stealPos];
        Integer[] myKeys = insertArray(insertIndex, stealKey, self.keys);
        String[] myValues = insertArray(insertIndex, stealValue, self.getValues());

        neighbor.setKeys(leftKeys);
        neighbor.setValues(leftValues);

        self.setKeys(myKeys);
        self.setValues(myValues);
    }

    private void mergeInner(InnerNode left, InnerNode right, Deque<InnerNode> parents) {
        InnerNode parent = parents.pop();

        // get key between left and right Node from parent
        int childIndex = Arrays.binarySearch(parent.getChildren(), right);
        int middleKey = parent.keys[childIndex - 1];

        // Concatenate left and right children and keys with parent key in between
        Integer[] keys = insertArray(left.keys.length, middleKey, left.keys);
        Node[] values = concatWithArrayCopy(left.getChildren(), right.getChildren());
        keys = concatWithArrayCopy(keys, right.keys);

        left.setKeys(keys);
        left.setPayload(values);

        // delete middle key and right child from parent
        parent.setKeys(deleteArray(childIndex - 1, parent.keys));
        parent.setChildren(deleteArray(childIndex, parent.getChildren()));

        // if left child not the first child
        // check for correct key
        if (childIndex > 1) {
            // parent.keys[childIndex - 2] = left.keys[0];
        }

        if (parent.keys.length >= capacity / 2) {
            return;
        }

        // parent is Root: capacity constraint is irrelevant
        if (parents.isEmpty()) {
            if (parent.keys.length == 0) {
                // When empty change left to root
                root = left;
            }
            return;
        }

        InnerNode parentParent = parents.peek();
        int indexInParentParent = Arrays.binarySearch(parentParent.getChildren(), parent);

        // try right
        if (indexInParentParent < parentParent.getChildren().length - 1) {
            InnerNode rightNeighbor = (InnerNode) parentParent.getChildren()[indexInParentParent + 1];
            if (rightNeighbor.keys.length == capacity / 2) {
                mergeInner(parent, rightNeighbor, parents);
                return;
            }
        }

        // try left
        if (indexInParentParent > 0) {
            InnerNode leftNeighbor = (InnerNode) parentParent.getChildren()[indexInParentParent - 1];
            if (leftNeighbor.keys.length == capacity / 2) {
                mergeInner(leftNeighbor, parent, parents);
                return;
            }
        }
    }

    private void mergeLeaf(LeafNode left, LeafNode right, Deque<InnerNode> parents) {
        InnerNode parent = parents.pop();
        String[] values = concatWithArrayCopy(left.getValues(), right.getValues());
        Integer[] keys = concatWithArrayCopy(left.keys, right.keys);
        left.setKeys(keys);
        left.setPayload(values);

        int childIndex = Arrays.binarySearch(parent.getChildren(), right);

        parent.setKeys(deleteArray(childIndex - 1, parent.keys));
        parent.setChildren(deleteArray(childIndex, parent.getChildren()));

        // if left child not the first child
        // check for correct key
        if (childIndex > 1) {
            // parent.keys[childIndex - 2] = left.keys[0];
        }

        if (parent.keys.length >= capacity / 2) {
            return;
        }

        // parent is Root: capacity constraint is irrelevant
        if (parents.isEmpty()) {
            if (parent.keys.length == 0) {
                // When empty change left to root
                root = left;
            }
            return;
        }

        InnerNode parentParent = parents.peek();
        int indexInParentParent = Arrays.binarySearch(parentParent.getChildren(), parent);

        // try right
        if (indexInParentParent < parentParent.getChildren().length - 1) {
            InnerNode rightNeighbor = (InnerNode) parentParent.getChildren()[indexInParentParent + 1];
            if (rightNeighbor.keys.length == capacity / 2) {
                mergeInner(parent, rightNeighbor, parents);
                return;
            }
        }

        // try left
        if (indexInParentParent > 0) {
            InnerNode leftNeighbor = (InnerNode) parentParent.getChildren()[indexInParentParent - 1];
            if (leftNeighbor.keys.length == capacity / 2) {
                mergeInner(leftNeighbor, parent, parents);
                return;
            }
        }
    }

    private String deleteFromLeafNode(Integer key, LeafNode node, Deque<InnerNode> parents) {
        // Done: delete value from leaf node (and propagate changes up)

        // Delete Key from Leaf
        int index = Arrays.binarySearch(node.keys, key);
        if (index < 0) {
            return null;
        }
        String[] values = deleteArray(index, node.getValues());
        Integer[] keys = deleteArray(index, node.keys);

        String deleted = node.getValues()[index];

        node.setKeys(keys);
        node.setValues(values);

        // Minimum Number of keys present
        if (node.keys.length >= ((int) Math.ceil(capacity / 2))) {
            return deleted;
        }

        // Leaf is also Root
        if (parents.isEmpty()) {
            return deleted;
        }

        // Too few keys in node
        // steal a key from a sibling node (left or right) if possible
        InnerNode parent = parents.peek();
        int indexInParent = Arrays.binarySearch(parent.keys, key);
        int keyIndexInParent = indexInParent;
        if (indexInParent < 0) {
            indexInParent = -(indexInParent + 1);
            keyIndexInParent = -(keyIndexInParent + 1);
        } else if (parent.keys[indexInParent] == key) {
            indexInParent++;
            keyIndexInParent++;
        }

        // try left
        LeafNode leftNeighbor = null;
        if (indexInParent > 0) {
            leftNeighbor = (LeafNode) parent.getChildren()[indexInParent - 1];

            if (leftNeighbor.keys.length > ((int) Math.ceil(capacity / 2))) {
                stealValue(leftNeighbor, node, leftNeighbor.keys.length - 1, 0);

                // Adjust keys in parent
                parent.keys[keyIndexInParent - 1] = node.keys[0];
                return deleted;
            }
        }

        // try right
        LeafNode rightNeighbor = null;
        if (indexInParent < parent.getChildren().length - 1) {
            rightNeighbor = (LeafNode) parent.getChildren()[indexInParent + 1];

            if (rightNeighbor.keys.length > ((int) Math.ceil(capacity / 2))) {
                stealValue(rightNeighbor, node, 0, node.keys.length);
                // Adjust keys in parent
                parent.keys[keyIndexInParent] = rightNeighbor.keys[0];
                return deleted;
            }
        }
        // If not
        // two siblings with minimal and sub-minimal number of keys can be merged
        if (rightNeighbor != null && rightNeighbor.keys.length == capacity / 2) {
            mergeLeaf(node, rightNeighbor, parents);
            return deleted;
        }
        if (leftNeighbor != null && leftNeighbor.keys.length == capacity / 2) {
            mergeLeaf(leftNeighbor, node, parents);
            return deleted;
        }

        return null;
    }

    ///// Public API
    ///// These can be left unchanged

    /**
     * Lookup the value stored under the given key.
     * 
     * @return The stored value, or {null} if the key does not exist.
     */
    public String lookup(Integer key) {
        LeafNode leafNode = findLeafNode(key, root);
        try {
            return lookupInLeafNode(key, leafNode);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Insert the key/value pair into the B+ tree.
     */
    public void insert(int key, String value) {
        Deque<InnerNode> parents = new LinkedList<>();
        LeafNode leafNode = findLeafNode(key, root, parents);
        try {
            insertIntoLeafNode(key, value, leafNode, parents);
        } catch (Exception e) {

        }

    }

    /**
     * Delete the key/value pair from the B+ tree.
     * 
     * @return The original value, or {null} if the key does not exist.
     */
    public String delete(Integer key) {
        Deque<InnerNode> parents = new LinkedList<>();
        LeafNode leafNode = findLeafNode(key, root, parents);
        try {
            return deleteFromLeafNode(key, leafNode, parents);
        } catch (Exception e) {
            return null;
        }

    }

    ///// Leave these methods unchanged

    private int capacity = 0;

    private Node root;

    public BPlusTree(int capacity) {
        this(new LeafNode(capacity), capacity);
    }

    public BPlusTree(Node root, int capacity) {
        assert capacity % 2 == 0;
        this.capacity = capacity;
        this.root = root;
    }

    public Node rootNode() {
        return root;
    }

    public String toString() {
        return new BPlusTreePrinter(this).toString();
    }

    private LeafNode findLeafNode(Integer key, Node node) {
        return findLeafNode(key, node, null);
    }

}
