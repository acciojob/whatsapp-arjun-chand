package com.driver;

import java.util.Date;
import java.util.List;

public class WhatsappService {
    WhatsappRepository whatsappRepository = new WhatsappRepository();
    public void createUser(String name, String mobNo) throws Exception {
        whatsappRepository.createUser(name, mobNo);
    }
    public Group createGroup(List<User> users){
       return whatsappRepository.createGroup(users);
    }

    public int createMessage(String content){
        return whatsappRepository.createMessage(content);
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception{
        return whatsappRepository.sendMessage(message, sender, group);
    }
    public void changeAdmin(User approver, User user, Group group) throws Exception{
        whatsappRepository.changeAdmin(approver,user, group);
    }
    public int removeUser(User user) throws Exception {
        return whatsappRepository.removeUser(user);
    }

    public String findMessage(Date start, Date end, int k) throws Exception {
        return whatsappRepository.findMessage(start, end, k);
    }
}
