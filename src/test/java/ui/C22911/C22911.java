package ui.C22911;

@SuiteName("1_SMOKE ТЕСТИРОВАНИЕ")
@TestName("09_Прейскуранты")
public class C22911 extends BaseUITest{

	private C22911_TestHelper helper;

	@BeforeMethod
	@Step("Предусловия:")
	public void setUpC22911() {
		helper = new C22911_TestHelper(app);
	}

	@DisplayedName("ADE")
	@Description(
		"<b>Precondition:</b>\n\n" +
		"<i>В системе есть прейскурант в статусе \"Черновик\"</i>\n\n" +
		"<b>Steps:</b>\n\n" +
		"01 - <i>Войти в систему\nВход выполнен</i>\n\n" +
		"02 - <i>Перейти на левой панели в раздел \"Прейскуранты\" -> ADE\nОткрыта вкладка с прейскурантом ADE</i>\n\n" +
		"03 - <i>Задублировать вкладку в браузере\nВкладка задублирована в браузере</i>\n\n" +
		"04 - <i>В выпадающем списке прейскурантов выбрать прейскурант из предусловия (в статусе \"Черновик\")\nПрейскурант выбран</i>\n\n" +
		"05 - <i>Нажать на кнопку \"Ввести в действие\" -> \"OK\"\nПоявляется лоадер и сообщение \"Прейскурант введен в действие успешно\"</i>\n\n" +
		"06 - <i>Перейти на задублированную вкладку\nПереход выполнен</i>\n\n" +
		"07 - <i>В выпадающем списке прейскурантов выбрать прейскурант из предусловия (в статусе \"Черновик\")\nПрейскурант выбран</i>\n\n" +
		"08 - <i>Нажать на кнопку \"Ввести в действие\" -> \"OK\"\nПоявляется красное сообщение об ошибке \"Возможно, кто-то уже ввел в действие этот прейскурант\"\n</i>")
	@Test(description="22911: Ввод в действие параллельно двумя пользователями")
	@TmsLink("22911")
	public void C22911Test(){
		step_01();
		step_02();
		step_03();
		step_04();
		step_05();
		step_06();
		step_07();
		step_08();
	}

	@Step("Шаг 1:")
	private void step_01(){
		helper.doLoginMat();
	}

	@Step("Шаг 2:")
	private void step_02(){
		helper.navigateToПрейскуранты();
		helper.selectTab();
		helper.precondition();
	}

	@Step("Шаг 3:")
	private void step_03(){
		helper.doubleTab();
	}

	@Step("Шаг 4:")
	private void step_04(){
		helper.assertThatDraftIsPresent();
	}

	@Step("Шаг 5:")
	private void step_05(){
		helper.assertThatEnactmentIsSucceed();
	}

	@Step("Шаг 6:")
	private void step_06(){
		helper.changeTab();
	}

	@Step("Шаг 7:")
	private void step_07(){
		helper.assertThatDraftIsPresent();
	}

	@Step("Шаг 8:")
	private void step_08(){
		helper.assertThatErrorMessageIsPresent();
	}
}
