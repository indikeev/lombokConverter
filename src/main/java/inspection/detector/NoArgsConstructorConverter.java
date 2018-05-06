package inspection.detector;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import util.LombokAnnotationsUtils;

/**
 * Created by 1 on 12.05.2018.
 */
public class NoArgsConstructorConverter extends NoArgsConstructorDetector {
    private Project project;

    public NoArgsConstructorConverter(Project project, PsiMethod psiMethod) {
        super(psiMethod);
        this.project = project;
    }

    public void convert() {
        final PsiClass psiClass = PsiTreeUtil.getParentOfType(psiMethod, PsiClass.class);
        if (psiClass == null)
            return;
        final PsiModifierList modifierList = psiClass.getModifierList();
        if (modifierList == null)
            return;

        final int accessLevel = PsiUtil.getAccessLevel(psiMethod.getModifierList());
        final String noArgConstructorAnnotation = LombokAnnotationsUtils.getNoArgConstructorAnnotation(accessLevel);
        final PsiAnnotation psiAnnotation = modifierList.addAnnotation(noArgConstructorAnnotation);
        psiMethod.delete();

        JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiAnnotation);
    }
}
