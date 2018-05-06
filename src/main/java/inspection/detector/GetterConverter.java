package inspection.detector;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiUtil;
import com.intellij.refactoring.JavaRefactoringFactory;
import com.intellij.refactoring.JavaRenameRefactoring;
import com.intellij.usageView.UsageInfo;
import org.jetbrains.annotations.NotNull;
import util.LombokAnnotationsUtils;

/**
 * Created by 1 on 12.05.2018.
 */
public class GetterConverter extends GetterDetector {
    private Project project;

    public GetterConverter(@NotNull Project project, @NotNull PsiMethod psiMethod) {
        super(psiMethod);
        this.project = project;
    }

    public void convert() {
        final String getterName = getGetterName(resolvedField);
        final String realGetterName = psiMethod.getName();

        if (!getterName.equals(realGetterName)) {
            if (!isRenameEnabled)
                return;
            renameMethod(project, getterName);
        }

        PsiModifierList modifierList = resolvedField.getModifierList();
        if (modifierList == null)
            return;

        final int accessLevel = PsiUtil.getAccessLevel(psiMethod.getModifierList());
        final String getterAnnotation = LombokAnnotationsUtils.getGetterAnnotation(accessLevel);

        final PsiAnnotation psiAnnotation = modifierList.addAnnotation(getterAnnotation);
        psiMethod.delete();

        JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiAnnotation);
    }

    private void renameMethod(@NotNull Project project, @NotNull String newName) {
        JavaRefactoringFactory instance = JavaRefactoringFactory.getInstance(project);
        JavaRenameRefactoring rename = instance.createRename(psiMethod, newName);
        UsageInfo[] usages = rename.findUsages();
        rename.doRefactoring(usages);
    }
}
