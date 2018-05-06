package inspection.tool;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import inspection.detector.logger.Log4jLoggerDetector;
import inspection.quickFixes.ConvertGetterToAnnotationFix;
import org.jetbrains.annotations.NotNull;

public class Log4jLoggerInspectionTool extends LombokConverterInspectionTool {
    private static final String DISPLAY_NAME = "log4j converter";

    private static final String SHORT_NAME = "log4j converter";

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
            }

            @Override
            public void visitField(PsiField field) {
                super.visitField(field);
                Log4jLoggerDetector detector = new Log4jLoggerDetector(field);
                if (detector.isLogger()) {
                    holder.registerProblem(field, "Convert getter to annotation", new ConvertGetterToAnnotationFix());
                }
            }
        };
    }

}
