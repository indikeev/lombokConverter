package inspection.tool;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ui.RegExFormatter;
import com.intellij.codeInspection.ui.RegExInputVerifier;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.DocumentAdapter;
import com.intellij.util.ui.CheckBox;
import com.intellij.util.ui.UIUtil;
import inspection.detector.GetterDetector;
import inspection.quickFixes.ConvertGetterToAnnotationFix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Created by 1 on 15.04.2018.
 */
public class GetterLocalInspectionTool extends LombokConverterInspectionTool {
    private static final String DISPLAY_NAME = "getter converter";

    private static final String SHORT_NAME = "getter converter";

    @NotNull
    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @NotNull
    @Override
    public String getShortName() {
        return SHORT_NAME;
    }


    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                GetterDetector detector = new GetterDetector(method);
                if (detector.isGetter()) {
                    holder.registerProblem(method, "Convert getter to annotation", new ConvertGetterToAnnotationFix());
                }
            }
        };
    }
}
