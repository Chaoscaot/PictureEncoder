package pictureencoder;

import java.util.LinkedList;

public class PictureDecodeResult {

    String name;
    private long lenght = 0;
    private Node current = null;
    private Node end = null;

    public String getName() {
        return name;
    }

    void add(byte b) {
        Node node = new Node(b);
        lenght++;
        if(lenght == 1) current = end = node;
        else end.next = node;
        end = node;
    }

    public byte nextByte() {
        if(lenght == 0) throw new ArrayIndexOutOfBoundsException();
        byte b = current.value;
        current = current.next;
        lenght--;
        return b;
    }

    public long available() {
        return lenght;
    }

    public boolean isEmpty() {
        return lenght == 0;
    }

    public boolean isNotEmpty() {
        return lenght != 0;
    }

    public byte[] getBytes(int lenght) {
        byte[] b = new byte[Math.min(lenght, (int)Math.min(this.lenght, Integer.MAX_VALUE))];
        for (int i = 0; i < b.length; i++) {
            b[i] = nextByte();
        }
        return b;
    }

    private class Node {

        private Node next = null;
        private byte value;

        public Node(byte value) {
            this.value = value;
        }

    }

    @Override
    public String toString() {
        return "PictureDecodeResult{" +
                "name='" + name + '\'' +
                ", lenght=" + lenght +
                ", current=" + current +
                ", end=" + end +
                '}';
    }
}
