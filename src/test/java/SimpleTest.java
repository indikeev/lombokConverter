import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import inspection.tool.GetterLocalInspectionTool;

public class SimpleTest extends LightCodeInsightFixtureTestCase {

    public void testSimpleGetterConversion() {
        doTest(true);
    }

    @Override
    protected String getBasePath() {
        return "testData/inspections";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.out.println("setup good");
        myFixture.addClass("package com.intellij.codeInspection; public class CommonProblemDescriptor {}");
        myFixture.addClass("package com.intellij.codeInspection; public class QuickFix {}");
//        myFixture.enableInspections(GetterLocalInspectionTool.class);
    }

    private void doTest(boolean fix) {
        myFixture.testHighlighting(getTestName(false) + ".java");
        if (!fix) return;

//        IntentionAction action = myFixture.filterAvailableIntentions("Properly capitalize").get(0);
//        WriteCommandAction.writeCommandAction(getProject()).run(() -> action.invoke(getProject(), myFixture.getEditor(), getFile()));
        myFixture.checkResultByFile(getTestName(false) + "_after.java");
    }
}
