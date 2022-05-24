package com.mawuote.client.modules.client.notifications;

import com.mawuote.client.modules.client.ModuleNotifications;

import java.util.ArrayList;

public class NotificationProcessor {
    public ArrayList<Notification> notifications = new ArrayList();

    public void handleNotifications(int posY) {
        for (int i = 0; i < this.getNotifications().size(); ++i) {
            if (this.getNotifications().get(i).animationUtils2.isDone() && this.getNotifications().get(i).didThing == false) {
                this.getNotifications().get(i).animationUtils.reset();
                this.getNotifications().get(i).didThing = true;

            }
            if(this.getNotifications().get(i).animationUtils.isDone() && this.getNotifications().get(i).isReversing == false) {
                if(this.getNotifications().get(i).timer.hasReached(this.getNotifications().get(i).disableTime - (ModuleNotifications.inOutTime.getValue().intValue()*2))) {
                    this.getNotifications().get(i).reverse.reset();
                    this.getNotifications().get(i).reverse2.reset();
                    this.getNotifications().get(i).isReversing = true;
                }
            }
            if(this.getNotifications().get(i).isReversing == true && this.getNotifications().get(i).reverse.isDone() && this.getNotifications().get(i).didFirstReverse == false) {
                this.getNotifications().get(i).reverse2.reset();
                this.getNotifications().get(i).didFirstReverse = true;
            }
            this.getNotifications().get(i).onDraw(posY);
            if(ModuleNotifications.addType.getValue()) {
                posY += 22;
            } else {
                posY -= 22;
            }
        }
    }

    public void addNotification(String text, long duration, long inOutTime) {
        this.getNotifications().add(new Notification(text, duration, inOutTime));
    }

    public ArrayList<Notification> getNotifications() {
        return this.notifications;
    }
}
