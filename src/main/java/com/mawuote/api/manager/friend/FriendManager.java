package com.mawuote.api.manager.friend;

import java.util.ArrayList;

public class FriendManager {
    private final ArrayList<Friend> friends;

    public FriendManager(){
        this.friends = new ArrayList<>();
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public Friend getFriend(String name) {
        return friends.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean isFriend(String name) {
        for (Friend friend : getFriends()) {
            if (friend.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public void addFriend(String name) {
        friends.add(new Friend(name));
    }

    public void removeFriend(String name) {
        if (getFriend(name) != null) {
            friends.remove(getFriend(name));
        }
    }

    public void clearFriends() {
        friends.clear();
    }
}
