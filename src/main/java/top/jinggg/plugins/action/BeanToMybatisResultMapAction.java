package top.jinggg.plugins.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import top.jinggg.plugins.converter.ConvertManager;
import top.jinggg.plugins.utils.StringUtils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class BeanToMybatisResultMapAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        PsiElement psiElement = psiFile.findElementAt(editor.getCaretModel().getOffset());

        StringBuilder builder = new StringBuilder();
        PsiClass beanClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        builder.append("<resultMap id=\""+beanClass.getName()+"ResultMap\" type=\""+beanClass.getQualifiedName()+"\">");

        PsiField[] psiFields = beanClass.getAllFields();
        for (PsiField psiField : psiFields) {
            String underlineField = StringUtils.toUnderLine(psiField.getName());
            builder.append("\n\t<result column=\""+underlineField+"\" jdbcType=\""+ ConvertManager.convert("postgres", psiField.getType().getCanonicalText()) +"\" property=\""+psiField.getName()+"\"/>");
        }
        builder.append("\n</resultMap>");

        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(builder.toString()), null);
        System.out.println(builder);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getRequiredData(CommonDataKeys.PSI_FILE);
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        boolean enable = psiFile instanceof PsiJavaFile && editor != null;
        e.getPresentation().setEnabledAndVisible(enable);
    }

    /**
     * 2022.3+版本后AnAction.getActionUpdateThread() 返回 ActionUpdateThread 线程
     * 决定了 update() 方法是否在 background thread (BGT) or the event-dispatching thread (EDT)上被调用。
     * 首选的方式是在BGT上执行update方法, 这样可以保证application侧读取 PSI, the virtual file system (VFS), 或 project models的有利条件。
     * 在BGT上运行update的Actions不应该直接访问Swing 组件的结构。 反之EDT上运行的update的Action必须禁止访问PSI, VFS, 或 project的数据，
     * 但可以访问Swing组件和其他UI模型。所有由DataContext 提供的可访问数据在这里有说明 Determining the Action Context.
     * 当必须从BGT切换回EDT时，需要使用 AnActionEvent.getUpdateSession()方法获得 UpdateSession 并调用 UpdateSession.compute() 在EDT上运行方法。
     * 从2022.3开始，插件的DevKit将会有检查 Plugin DevKit | Code | ActionUpdateThread 是否丢失，来提醒插件作者实现这个方法
     * @return
     */
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
