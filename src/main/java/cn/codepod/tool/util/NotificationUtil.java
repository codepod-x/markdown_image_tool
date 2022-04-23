package cn.codepod.tool.util;

import cn.codepod.tool.icon.MarkdownIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * @author zhanglei
 * @date 2022/4/4 10:45
 */
public class NotificationUtil {

    public static void show(NotificationType type, String content) {
        Notification notification = NotificationGroupManager.getInstance()
                .getNotificationGroup("cn.codepod.tool.notification")
                .createNotification("Markdown image tool", content, type, null)
                .setIcon(MarkdownIcons.DEFAULT);
        Notifications.Bus.notify(notification);
    }

    public static void show(String content) {
        show(NotificationType.INFORMATION, content);
    }
}
