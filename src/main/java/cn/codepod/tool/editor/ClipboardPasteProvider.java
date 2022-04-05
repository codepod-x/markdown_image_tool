package cn.codepod.tool.editor;

import cn.codepod.tool.util.MarkdownUtil;
import cn.codepod.tool.util.NotificationUtil;
import cn.codepod.tool.util.OssUtil;
import com.aliyun.oss.OSSException;
import com.intellij.ide.PasteProvider;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.ImageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.MultiResolutionImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhanglei
 * @date 2022/4/3 19:16
 */
public class ClipboardPasteProvider implements PasteProvider {

    @Override
    public void performPaste(@NotNull DataContext dataContext) {
        VirtualFile currentFile = dataContext.getData(CommonDataKeys.VIRTUAL_FILE);
        if (currentFile == null) {
            return;
        }
        Transferable pasteContents = CopyPasteManager.getInstance().getContents();
        if (pasteContents == null) {
            return;
        }
        BufferedImage imageToPaste;
        try {
            Object image = pasteContents.getTransferData(DataFlavor.imageFlavor);
            imageToPaste = toBufferedImage(image);
        } catch (UnsupportedFlavorException e) {
            NotificationUtil.show(NotificationType.ERROR, "Image is unsupported.");
            return;
        } catch (IOException e) {
            NotificationUtil.show(NotificationType.ERROR, "Failed to get data from the clipboard. Data is no longer available. Aborting operation.");
            return;
        }

        if (imageToPaste == null) {
            NotificationUtil.show(NotificationType.ERROR, "Failed to get data from the clipboard.");
            return;
        }

        ByteArrayOutputStream os = null;
        InputStream is = null;
        try {
            os = new ByteArrayOutputStream();
            ImageIO.write(imageToPaste, "png", os);
            is = new ByteArrayInputStream(os.toByteArray());
            os.close();
            Editor editor = dataContext.getData(CommonDataKeys.EDITOR);
            String markdownFileName = MarkdownUtil.getMarkdownFileName(editor, false);
            String markdownImageText = OssUtil.upload(is, markdownFileName);
            is.close();
            NotificationUtil.show(NotificationType.INFORMATION, "File from clipboard is uploaded.");
            if (editor != null) {
                Caret caret = editor.getCaretModel().getCurrentCaret();
                Document document = editor.getDocument();
                WriteCommandAction.runWriteCommandAction(editor.getProject(), null, null, () -> {
                    document.insertString(caret.getOffset(), markdownImageText + '\n');
                    caret.moveToOffset(caret.getOffset() + markdownImageText.length() + 1);
                });
            } else {
                Messages.showInfoMessage("File is Uploaded, but insert text is failed.", "Markdown Image Tool");
            }
        } catch (IOException e) {
            NotificationUtil.show(NotificationType.ERROR, "Failed to trans image to stream.");
        } catch (OSSException e) {
            NotificationUtil.show(NotificationType.ERROR, "Failed to upload.");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // just skip
            }
        }

    }

    @Override
    public boolean isPastePossible(@NotNull DataContext dataContext) {
        return true;
    }

    @Override
    public boolean isPasteEnabled(@NotNull DataContext dataContext) {
        VirtualFile virtualFile = dataContext.getData(CommonDataKeys.VIRTUAL_FILE);
        return CopyPasteManager.getInstance().areDataFlavorsAvailable(DataFlavor.imageFlavor)
                && virtualFile != null
                && dataContext.getData(CommonDataKeys.EDITOR) != null
                && "Markdown".equals(virtualFile.getFileType().getName());
    }

    private BufferedImage toBufferedImage(@Nullable Object img) {
        Image image;
        if (img == null) {
            return null;
        }
        if (img instanceof MultiResolutionImage) {
            image = ((MultiResolutionImage) img).getResolutionVariants().stream().findFirst().orElse(null);
        } else if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        } else if (img instanceof Image) {
            image = (Image) img;
        } else {
            return null;
        }
        if (image == null) {
            return null;
        }
        return ImageUtil.toBufferedImage(image);
    }
}
