package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private List<User> userDB;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;
    int groupCount = 0;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }
    void createUser(String name, String mobNo) throws Exception{
        if(userMobile.contains(mobNo))
            throw new Exception("User already exists");
        User user = new User(name, mobNo);
        userMobile.add(mobNo);
    }
    Group createGroup(List<User> users){
        Group group = new Group();
        if(users.size() == 2){
            group.setName(users.get(0).getName());
        }
        else {
            ++customGroupCount;
            group.setName("Group "+customGroupCount);
        }
        group.setNumberOfParticipants(users.size());

        adminMap.put(group,users.get(0));
        groupUserMap.put(group,users);
        groupMessageMap.put(group, new ArrayList<>());

        return group;
    }

    int createMessage(String content){
        ++messageId;
        Message message = new Message(messageId, content);
        message.setTimestamp(new Date());

        return message.getId();
    }
    int sendMessage(Message message, User sender, Group group)throws Exception{
        if (!groupUserMap.containsKey(group))
            throw new Exception("Group does not exist");

        if (!groupUserMap.containsValue(sender))
            throw new Exception("You are not allowed to send message");

        senderMap.put(message,sender);
        groupMessageMap.get(group).add(message);

        return groupMessageMap.get(group).size();
    }

    void changeAdmin(User approver, User user, Group group) throws Exception{
        if (!groupUserMap.containsKey(group))
            throw new Exception("Group does not exist");
        if (!adminMap.containsKey(approver))
            throw new Exception("Approver does not have rights");
        if (!groupUserMap.containsValue(user))
            throw new Exception("User is not a participant");

        adminMap.put(group,user);
    }
    public int removeUser(User user) throws Exception {
        Group group = null;
        for (Group gr : groupUserMap.keySet())
            if (groupUserMap.get(gr).contains(user)) {
                group = gr;
                break;
            }
        if (group == null)
            throw new Exception("User not found");

        if (adminMap.get(group).equals(user))
            throw new Exception("Cannot remove admin");

        groupUserMap.get(group).remove(user);
        group.setNumberOfParticipants(group.getNumberOfParticipants() - 1);

        for (Message message : senderMap.keySet())
            if (senderMap.get(message).equals(user)) {
                senderMap.remove(message);
                groupMessageMap.get(group).remove(message);
                --messageId;
            }

        return group.getNumberOfParticipants() + groupMessageMap.get(group).size() + messageId;
    }

    public String findMessage(Date start, Date end, int k) throws Exception {
        List<Message> messageList = new ArrayList<>();

        for (Message message : senderMap.keySet()) {
            Date timestamp = message.getTimestamp();
            if (timestamp.after(start) && timestamp.before(end))
                messageList.add(message);
        }

        if (messageList.size() < k)
            throw new Exception("K is greater than the number of messages");

        return messageList.get(k).getContent();
    }
}

