package inspection.tool;

import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethod;
import inspection.detector.NoArgsConstructorDetector;
import inspection.quickFixes.ConverterNoArgsConstructorToAnnotationFix;
import org.jetbrains.annotations.NotNull;

/**
 * Created by 1 on 12.05.2018.
 */
public class NoArgsConstructorLocalInspectionTool extends BaseJavaLocalInspectionTool {
    @NotNull
    @Override
    public String getDisplayName() {
        return "DISPLAY_NAME";
    }

    @NotNull
    @Override
    public String getShortName() {
        return "SHORT_NAME";
    }

    @NotNull
    @Override
    public String getGroupDisplayName() {
        return "group name";
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                NoArgsConstructorDetector detector = new NoArgsConstructorDetector(method);
                if (detector.isNoArgsConstructor()) {
                    holder.registerProblem(method, "Convert NoArgsConstructor to annotation", new ConverterNoArgsConstructorToAnnotationFix());
                }
            }
        };
    }
}
