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
    public String excludeClassNames = "";  // must be public for JDOMSerialization
    private Pattern excludeClassNamesPattern;

    /**
     * User options for excluded exception classes
     */
    public boolean excludeException = true; // must be public for JDOMSerialization
    /**
     * User options for excluded deprecated classes
     */
    public boolean excludeDeprecated = true; // must be public for JDOMSerialization
    /**
     * User options for excluded enum classes
     */
    public boolean excludeEnum; // must be public for JDOMSerialization
    /**
     * User options for excluded abstract classes
     */
    public boolean excludeAbstract; // must be public for JDOMSerialization

    public boolean excludeTestCode;

    public boolean excludeInnerClasses;

    // Appears under Settings > Inspections > Hibernate inspections.
    private static final String DISPLAY_NAME = "getter converter";

    // Error tooltip that appears in the editor.
    private static final String DESCRIPTION_TEMPLATE = "Persisted class cannot be final.";

    private static final String SHORT_NAME = "PersistedClassIsFinal";

    private static final String QUICK_FIX__REMOVE_FINAL_MODIFIER = "Remove 'final' modifier";

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


    @Nullable
    @Override
    public String getStaticDescription() {
        return "Custom Description";
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
                GetterDetector detector = new GetterDetector(method);
                if (detector.isGetter()) {
                    holder.registerProblem(method, "Convert getter to annotation", new ConvertGetterToAnnotationFix());
                }
            }
        };
    }

    @Override
    public JComponent createOptionsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Exclude classes (reg exp):"), constraints);

        final JFormattedTextField excludeClassNamesField = new JFormattedTextField(new RegExFormatter());
        excludeClassNamesField.setValue(excludeClassNamesPattern);
        excludeClassNamesField.setColumns(25);
        excludeClassNamesField.setInputVerifier(new RegExInputVerifier());
        excludeClassNamesField.setFocusLostBehavior(JFormattedTextField.COMMIT);
        excludeClassNamesField.setMinimumSize(excludeClassNamesField.getPreferredSize());
        UIUtil.fixFormattedField(excludeClassNamesField);
        Document document = excludeClassNamesField.getDocument();
        document.addDocumentListener(new DocumentAdapter() {

            @Override
            protected void textChanged(DocumentEvent e) {
                try {
                    excludeClassNamesField.commitEdit();
                    excludeClassNamesPattern = (Pattern) excludeClassNamesField.getValue();
                    excludeClassNames = excludeClassNamesPattern.pattern();
                } catch (final Exception ignore) {
                }
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.NONE;
        panel.add(excludeClassNamesField, constraints);

        final CheckBox excludeExceptionCheckBox = new CheckBox("Ignore exception classes", this, "excludeException");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(excludeExceptionCheckBox, constraints);

        final CheckBox excludeDeprecatedCheckBox = new CheckBox("Ignore deprecated classes", this, "excludeDeprecated");
        constraints.gridy = 2;
        panel.add(excludeDeprecatedCheckBox, constraints);

        final CheckBox excludeEnumCheckBox = new CheckBox("Ignore enum classes", this, "excludeEnum");
        constraints.gridy = 3;
        panel.add(excludeEnumCheckBox, constraints);

        final CheckBox excludeAbstractCheckBox = new CheckBox("Ignore abstract classes", this, "excludeAbstract");
        constraints.gridy = 4;
        panel.add(excludeAbstractCheckBox, constraints);

        final CheckBox excludeInTestCodeCheckBox = new CheckBox("Ignore test classes", this, "excludeTestCode");
        constraints.gridy = 5;
        panel.add(excludeInTestCodeCheckBox, constraints);

        final CheckBox excludeInnerClasses = new CheckBox("Ignore inner classes", this, "excludeInnerClasses");
        constraints.gridy = 6;
        constraints.weighty = 1.0;
        panel.add(excludeInnerClasses, constraints);

        return panel;
    }
}
