package inspection.quickFixes;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import inspection.detector.GetterConverter;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by 1 on 17.04.2018.
 */
public class ConvertGetterToAnnotationFix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Convert getter to annotation";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "Lombok converter";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiElement psiElement = descriptor.getPsiElement();
        assert psiElement instanceof PsiMethod; // TODO do we need it?
        PsiMethod psiMethod = (PsiMethod) psiElement;
        GetterConverter detector = new GetterConverter(project, psiMethod);
        if (detector.isGetter()) {
            detector.convert();
        }
    }
}