package rest.C23016;

@SuiteName("1_SMOKE ТЕСТИРОВАНИЕ")
@TestName("09_Прейскуранты")
public class C23016 extends BaseApiTest {

	private C23016_TestHelper helper;

	@BeforeMethod
	@Step("Предусловия:")
	public void setUpC23016() {
		helper = new C23016_TestHelper(app);
	}

	@DisplayedName("API: Расчет стоимости цветных камней по номинальным значениям")
	@Description(
		"<b>Precondition:</b>\n\n" +
		"<b>Steps:</b>\n\n" +
		"01 - <i>Выполнить POST запрос:\n\n{\n\"weight\": 1.25,\n\"colorintensityId\": 9,\n\"qualId\": **151**,\n\"quantityOfStones\": 1,\n\"colorBaseId\": 12,\n\"colorTone1Id\": 1,\n\"colorTone2Id\": 1,\n\"formId\": 1117\n}\nВернулся ответ с кодом ответа \"200\" - Стоимость рассчитана успешно.\nЗапомнить рассчитанные значения стоимости;\nПроверить корректное переопределение для следующих параметров качества\n\n|||:QualID	|:Name |:Переопр. кач-во\n||151	|VVS1 | 151\n||153	|VS1 | 153\n||162	|VVS | 151\n||163	|VS | 153\n||165	|FL | 150\nСтоимости для переопределенных параметров кач-ва рассчитываются и равны стоимости в которые они переопределены</i>\n\n" +
		"02 - <i>Проверить корректное переопределение для следующих параметров intensity\nзначения 1, 3, 5 должны переопределяться в значение 2\nСтоимости для переопределенных параметров intensity рассчитываются и равны стоимости в которые они переопределены</i>")
	@Test(description="23016: Проверка переопределения CalcPriceRCT")
	@TmsLink("23016")
	public void C23016Test(){
		step_01();
		step_02();
	}

	@Step("Шаг 1:")
	private void step_01(){
		helper.verifyQualSubstitutions();
	}

	@Step("Шаг 2:")
	private void step_02(){
		helper.verifyIntensitySubstitutions();
	}
}
