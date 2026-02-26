package mock;
import java.util.*;

class Message {
    String content;
    long timestamp;

    public Message(String content) {
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }
}

interface IMessageStore {
    void storeMessages(Queue<Message> messages);
    Queue<Message> getAllMessages();
}

interface IMessageQueue {
    void sendMessage(String content);
    int getTotalMessagesSent();
    int getPendingMessages();
    void flush(); // Send all pending messages immediately
}

class MessageStore implements IMessageStore{
    Queue<Message> messageQueue = new LinkedList<>();
    @Override
    public void storeMessages(Queue<Message> messages) {
        while(!messages.isEmpty())
            messageQueue.add(messages.poll());

        System.out.println("Message Batch Stored");
    }

    @Override
    public Queue<Message> getAllMessages() {
        return new LinkedList<>(messageQueue);
    }
}

class MessageQueue implements IMessageQueue{
    IMessageStore messageStore;
    Queue<Message> message = new LinkedList<>();
    int batchSize;

    public MessageQueue(IMessageStore messageStore, int batchSize) {
        this.messageStore = messageStore;
        this.batchSize = batchSize;
    }

    @Override
    public void sendMessage(String content) {
        Message mess = new Message(content);
        message.add(mess);

        if(message.size()>= batchSize){
            messageStore.storeMessages(message);
        }
    }

    @Override
    public int getTotalMessagesSent() {
        return messageStore.getAllMessages().size();
    }

    @Override
    public int getPendingMessages() {
        return message.size();
    }

    @Override
    public void flush() {
        messageStore.storeMessages(message);
    }
}

public class _05_MessageQueue {
    public static void main(String[] args) {
        // Create store and queue with batch size of 3
        IMessageStore store = new MessageStore();
        IMessageQueue queue = new MessageQueue(store, 3);

        System.out.println("=== Message Queue Demo (Batch Size: 3) ===\n");

        // Send messages one by one
        queue.sendMessage("Hello World");
        queue.sendMessage("How are you?");
        queue.sendMessage("Good morning");  // Auto-flush happens here

        System.out.println();
        queue.sendMessage("Welcome");
        queue.sendMessage("Testing 123");

        System.out.println("\n--- Current Status ---");
        System.out.println("Total messages sent: " + queue.getTotalMessagesSent());
        System.out.println("Pending messages: " + queue.getPendingMessages());

        // Manual flush
        System.out.println("\n--- Manual Flush ---");
        queue.flush();

        System.out.println("\n--- Final Status ---");
        System.out.println("Total messages sent: " + queue.getTotalMessagesSent());
        System.out.println("Pending messages: " + queue.getPendingMessages());

        // Show all messages
        System.out.println("\n--- All Stored Messages ---");
        Queue<Message> allMessages = store.getAllMessages();
        int count = 1;
        for (Message msg : allMessages) {
            System.out.println(count++ + ". " + msg.content);
        }
    }
}