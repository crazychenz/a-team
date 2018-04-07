package clueless;

/**
 * Represents an item in a (internally) linked list.
 *
 * <p>Note: This linked list ListItem is modeled off the Linux Kernel list.h
 *
 * @author ateam
 */
public abstract class ListItem {

    public ListItem prev;
    public ListItem next;

    /** Constructor */
    public ListItem() {
        init();
    }

    /** (Re-) Initializer of linked list item */
    public void init() {
        prev = this;
        next = this;
    }

    /**
     * Fetch the next item
     *
     * @return ListItem of next item in list
     */
    public ListItem getNext() {
        return next;
    }

    /**
     * Fetch the previous item
     *
     * @return ListItem of previous item in list
     */
    public ListItem getPrev() {
        return prev;
    }

    private static void _add(ListItem nitem, ListItem prev, ListItem next) {
        next.prev = nitem;
        nitem.next = next;
        nitem.prev = prev;
        prev.next = nitem;
    }

    /**
     * Add list item to tail.
     *
     * @param nitem New item to add to linked list.
     * @param list List to add new item to.
     */
    public static void addToTail(ListItem nitem, ListItem list) {
        // This effectively adds to tail.
        _add(nitem, list.prev, list);
    }
}
