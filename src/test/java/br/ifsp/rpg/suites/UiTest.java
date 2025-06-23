package br.ifsp.rpg.suites;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages({"br.ifsp.rpg"})
@SuiteDisplayName("All Ui tests")
@IncludeTags({"UiTest"})
public class UiTest {
}
