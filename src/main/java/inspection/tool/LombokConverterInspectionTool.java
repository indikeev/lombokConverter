package inspection.tool;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import org.jetbrains.annotations.NotNull;

public class LombokConverterInspectionTool extends AbstractBaseJavaLocalInspectionTool {
    private static final String LOMBOK_CONVERTER_GROUP_NAME = "lombok converter";

    @NotNull
    @Override
    public String getGroupDisplayName() {
        return LOMBOK_CONVERTER_GROUP_NAME;
    }


    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

}
