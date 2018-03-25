package clueless;

public abstract class ListItem /*implements Iterable<ListItem>*/ {

    public ListItem prev;
    public ListItem next;

    public ListItem() {
        prev = this;
        next = this;
    }

    public ListItem getNext() {
        return next;
    }

    public void insertBefore(ListItem elem) {
        elem.prev = prev;
        elem.next = this;
        prev.next = elem;
        prev = elem;
    }

    public void insertAfter(ListItem elem) {
        elem.prev = this;
        elem.next = next;
        next.prev = elem;
        next = elem;
    }

    public static void addItem(ListItem list, ListItem nitem) {
        if (list == null) {
            list = nitem;
            return;
        }

        // This effectively adds to tail.
        list.insertBefore(nitem);
    }

    /*@Override
    public Iterator<ListItem> iterator() {
        return new ListItemIterator(this);
    }

    public class ListItemIterator implements Iterator<ListItem> {
        ListItem head;
        ListItem item;

        public ListItemIterator(ListItem list) {
            if (list == null) {
                head = null;
                return;
            }
            head = list;
            item = head;
        }

        @Override
        public boolean hasNext() {
            if (head == null || item == null) {
                return false;
            }
            return true;
        }

        @Override
        public ListItem next() {
            if (!hasNext()) {
                // TODO Is this correct?
                return null;
            }

            item = item.getNext();
            return item;
        }
    }*/
}
