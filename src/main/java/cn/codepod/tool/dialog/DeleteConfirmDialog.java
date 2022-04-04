package cn.codepod.tool.dialog;

import cn.codepod.tool.entity.MarkdownImage;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.VerticalFlowLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhanglei
 * @date 2022/4/4 18:31
 */
public class DeleteConfirmDialog extends DialogWrapper {

    private final List<MarkdownImage> images;
    private final Set<MarkdownImage> selected = new HashSet<>();

    public DeleteConfirmDialog(List<MarkdownImage> images) {
        super(true);
        this.images = images;
        setTitle("Delete Image");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new VerticalFlowLayout());
        JLabel label = new JLabel("Please select which you want to delete");
        label.setFont(new Font(null, Font.BOLD, 12));
        panel.add(label, VerticalFlowLayout.TOP);
        JPanel imgPanel = new JPanel(new VerticalFlowLayout());
        for (MarkdownImage image : images) {
            JCheckBox checkBox = new JCheckBox(image.getDescription(), false);
            checkBox.addChangeListener(e -> {
                JCheckBox box = (JCheckBox) e.getSource();
                if (box.isSelected()) {
                    selected.add(image);
                } else {
                    selected.remove(image);
                }
            });
            imgPanel.add(checkBox);
        }
        panel.add(imgPanel);
        return panel;
    }

    public Set<MarkdownImage> getSelected() {
        return selected;
    }
}
