package nebula.module;

public class PetDBMapperTest extends TestBase {

	String clazz = PetDBMapper.class.getName();

//	@Test
//	public void testPrint() throws IOException {
//		System.out.println(RefineCode.refineCode(toString(clazz)));
//	}
//
//	@Test
//	public void testConstructerEmpty() throws IOException {
//		String targetClazz = Pet.class.getName();
//
//		ClassBody cw = ClassBuilder.make(clazz).implement(RowMapper.class, targetClazz).body();
//
//		cw.constructerEmpty();
//
//		cw.publicMethod(targetClazz, "map")
//			.tHrow(SQLException.class)
//			.parameter("rs", ResultSet.class)
//			.parameter("ctx", StatementContext.class)
//			.code(mv -> {
//				mv.line();
//				mv.NEW(targetClazz);
//				mv.DUP();
//
//				{
//					String name = "id";
//					String jdbcFuncName = "getLong";
//					Class<?> jdbcType = long.class;
//					Class<?> pojoType = Long.class;
//
//					mv.LOAD("rs");
//					mv.LOADConst(name);
//					mv.INTERFACE(ResultSet.class, jdbcFuncName).parameter(String.class).reTurn(jdbcType).INVOKE();
//					mv.STATIC(Long.class, "valueOf").parameter(long.class).reTurn(pojoType).INVOKE();
//				}
//				{
//					String name = "name";
//					String jdbcFuncName = "getString";
//					Class<?> jdbcClass = String.class;
//					Class<?> pojoClass = String.class;
//
//					mv.LOAD("rs");
//					mv.LOADConst(name);
//					mv.INTERFACE(ResultSet.class, jdbcFuncName).parameter(jdbcClass).reTurn(pojoClass).INVOKE();
//				}
//
//				{
//					String name = "desciption";
//					String jdbcFuncName = "getString";
//					Class<?> jdbcClass = String.class;
//					Class<?> pojoClass = String.class;
//
//					mv.LOAD("rs");
//					mv.LOADConst(name);
//					mv.INTERFACE(ResultSet.class, jdbcFuncName).parameter(jdbcClass).reTurn(pojoClass).INVOKE();
//				}
//
//				mv.INVOKESPECIAL(targetClazz, null, "<init>", Long.class.getName(), String.class.getName(),
//						String.class.getName());
//
//				mv.RETURNTop();
//			});
//
//		cw.method(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, Object.class.getName(), "map")
//			.tHrow(SQLException.class)
//			.parameter("rs", ResultSet.class)
//			.parameter("ctx", StatementContext.class)
//			.code(mv -> {
//				mv.line();
//				mv.LOAD(0);
//				mv.LOAD(1);
//				mv.LOAD(2);
//				mv.INVOKEVIRTUAL(cw.getName(), targetClazz, "map", ResultSet.class.getName(),
//						StatementContext.class.getName());
//				mv.RETURNTop();
//			});
//
//		String codeActual = toString(cw.end().toByteArray());
//		String codeExpected = toString(clazz);
//		assertEquals("Code", codeExpected, codeActual);
//	}
}
