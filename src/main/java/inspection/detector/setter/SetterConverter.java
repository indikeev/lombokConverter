package inspection.detector.setter;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import util.LombokAnnotationsUtils;

public class SetterConverter extends SetterDetector {
    private final Project project;
    private PsiModifierList modifierList;

    public SetterConverter(Project project, @NotNull PsiMethod method) {
        super(method);
        this.project = project;
    }

    public void convert() {
        modifierList = leftResolveField.getModifierList();
        if (modifierList == null)
            return;


        final int accessLevel = PsiUtil.getAccessLevel(method.getModifierList());
        final String setterAnnotation = LombokAnnotationsUtils.getGetterAnnotation(accessLevel);

        final PsiAnnotation annotation = modifierList.addAnnotation(setterAnnotation);
        method.delete();

        JavaCodeStyleManager.getInstance(project).shortenClassReferences(annotation);
    }
}
