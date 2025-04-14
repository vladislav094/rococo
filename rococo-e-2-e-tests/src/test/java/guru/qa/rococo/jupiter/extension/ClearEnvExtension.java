package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.jdbc.DataSources;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class ClearEnvExtension implements SuiteExtension {

    private final Config CFG = Config.getInstance();
    private final JdbcTemplate authDb = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    private final JdbcTemplate artistDb = new JdbcTemplate(DataSources.dataSource(CFG.artistJdbcUrl()));
    private final JdbcTemplate museumDb = new JdbcTemplate(DataSources.dataSource(CFG.museumJdbcUrl()));
    private final JdbcTemplate paintingDb = new JdbcTemplate(DataSources.dataSource(CFG.paintingJdbcUrl()));
    private final JdbcTemplate userdataDb = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));

    @Override
    public void beforeAll(ExtensionContext context) throws Exception{
        SuiteExtension.super.beforeAll(context);
    }

    @Override
    public void afterSuite() {
//        authDb.execute("TRUNCATE TABLE authority, \"user\" CASCADE;");
//        artistDb.execute("TRUNCATE TABLE artist CASCADE;");
//        museumDb.execute("TRUNCATE TABLE museum, geo CASCADE;");
//        paintingDb.execute("TRUNCATE TABLE painting CASCADE;");
//        userdataDb.execute("TRUNCATE TABLE \"user\"  CASCADE;");
    }

}
