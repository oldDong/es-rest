package com.dongzj.es.common.util;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2018/11/8
 * Time: 17:07
 */
public class SQLUtils {

    private static SqlSessionFactory factory = null;

    private static void init() {
        InputStream is = SQLUtils.class.getClassLoader().getResourceAsStream("sqlutils-config.xml");
        factory = new SqlSessionFactoryBuilder().build(is);
    }

    public static String getSql(String id, Object parameterObject) {
        if (null == factory) {
            init();
        }
        MappedStatement mappedStatement = factory.getConfiguration().getMappedStatement(id);
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        return boundSql.getSql().replaceAll("\t|\r|\n", " ").replaceAll(" +", " ");
    }

    public static String wrap(String original, WrapSymbol symbol) {
        return symbol.getSymbole() + original + symbol.getSymbole();
    }

    public enum WrapSymbol {
        SINGLE_QUOTE("'"),
        BACK_QUOTE("`");

        private String symbol;

        WrapSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbole() {
            return symbol;
        }
    }
}
