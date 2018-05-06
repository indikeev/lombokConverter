package inspection.tool;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethod;
import inspection.detector.setter.SetterDetector;
import inspection.quickFixes.ConvertSetterToAnnotationFix;
import org.jetbrains.annotations.NotNull;

public class SetterLocalInspectionTool extends LombokConverterInspectionTool {
    private static final String DISPLAY_NAME = "setter converter";

    private static final String SHORT_NAME = "setter converter";

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
                SetterDetector detector = new SetterDetector(method);
                if (detector.isSetter()) {
                    holder.registerProblem(method, "Convert setter to annotation", new ConvertSetterToAnnotationFix());
                }
            }
        };
    }
}
