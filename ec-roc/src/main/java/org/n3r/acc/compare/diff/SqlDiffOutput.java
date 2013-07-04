package org.n3r.acc.compare.diff;

import org.apache.commons.lang3.StringUtils;
import org.n3r.acc.compare.Record;
import org.n3r.acc.compare.impl.DiffStat;
import org.n3r.acc.utils.AccUtils;
import org.n3r.config.Configable;
import org.n3r.core.tag.*;
import org.n3r.esql.Esql;

import java.util.Map;

@EcRocTag("SqlDiff")
public class SqlDiffOutput implements DiffOutput, FromSpecConfig<SqlDiffOutput> {
    private String batchNo;
    private Esql esqlData;
    private Esql esqlStat;
    private OnlyLeftDiffListener onlyLeftDiffListener;

    @Override
    public void onlyRight(Record record) {
        logDiff(DiffMode.OnlyRight, null, record, null);
    }

    private void logDiff(DiffMode diffMode, Record lRecord, Record rRecord, String diffs) {
        switch (diffMode) {
            case OnlyRight:
                esqlData.params(batchNo, "0R", null, rRecord.toString(), null, null, rRecord.getKey()).execute();
                break;
            case OnlyLeft:
                esqlData.params(batchNo, "L0", lRecord.toString(), null, null, lRecord.getKey(), null).execute();
                break;
            case Diff:
                esqlData.params(batchNo, "LR", lRecord.toString(), rRecord.toString(), diffs,
                        lRecord.getKey(), rRecord.getKey()).execute();
            case Balance:
                break;
        }
    }

    @Override
    public Record onlyLeft(Record record) {
        Record rightRecord = null;
        if (onlyLeftDiffListener != null)  {
            rightRecord = onlyLeftDiffListener.onLeftRight(record);
        }

        if (rightRecord == null) logDiff(DiffMode.OnlyLeft, record, null, null);

        return rightRecord;
    }

    @Override
    public void diff(Record leftRecord, Record rightRecord, String diffs) {
        logDiff(DiffMode.Diff, leftRecord, rightRecord, diffs);
    }

    @Override
    public void balance(Record leftRecord, Record rightRecord) {
        logDiff(DiffMode.Balance, leftRecord, rightRecord, null);
    }


    @Override
    public void startCompare(String batchNo) {
        this.batchNo = batchNo;
    }

    @Override
    public void endCompare(DiffStat diffStat) {
        esqlStat.params(batchNo, diffStat.getOnlyLefts(), diffStat.getOnlyRights(), diffStat.getDiffs()
                , diffStat.getBalances(), diffStat.getStartTime(), diffStat.getEndTime(), diffStat.getTotal(), diffStat.getCostTime()).execute();

    }


    @Override
    public SqlDiffOutput fromSpec(Configable config, Map<String, String> context) {
        String connectionName = config.getStr("diff.connectionName", Esql.DEFAULT_CONN_NAME);
        String sqlid = config.getStr("diff.sqlid");
        String sqlidStat = config.getStr("diff.sqlidStat");
        String sqlfile = config.getStr("diff.sqlfile");
        esqlData = new Esql(connectionName).useSqlFile(sqlfile).id(sqlid);
        esqlStat = new Esql(connectionName).useSqlFile(sqlfile).id(sqlidStat);

        String diffListener = config.getStr("diff.listener");
        if (StringUtils.isNotEmpty(diffListener))
            this.onlyLeftDiffListener = AccUtils.parseSpec(diffListener, OnlyLeftDiffListener.class, config, context);

        return this;
    }
}
