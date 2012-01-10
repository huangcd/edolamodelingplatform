package cn.edu.tsinghua.thss.tsmart.modeling.validation;

public class Rule {
    String  firstEntity;
    String  secondEntity;
    String  relation;
    boolean needCheckOnline; // 是否需要在线检查
    int     max;
    int     min;

    public String getDescription() {
        if (relation.equals(Relation.CONNECT)) {
            if (max == Integer.MAX_VALUE)
                return String.format("%s 只能和 %d 个以上的 %s 相连", firstEntity, min, secondEntity);
            else if (max == min)
                return String.format("%s 只能和 %d 个 %s 相连", firstEntity, min, secondEntity);
            else
                return String.format("%s 只能和 %d 到 %d 个 %s 相连", firstEntity, min, max, secondEntity);
        }
        if (relation.equals(Relation.EQUAL)) {
            return String.format("%s 与  %s 相等", firstEntity, secondEntity);
        }
        if (relation.equals(Relation.GREAT_EQUAL)) {
            return String.format("%s 大于等于  %s", firstEntity, secondEntity);
        }
        if (relation.equals(Relation.GREAT_THAN)) {
            return String.format("%s 大于  %s", firstEntity, secondEntity);
        }
        if (relation.equals(Relation.HAS)) {
            if (max == Integer.MAX_VALUE)
                return String.format("%s 必须包含 %d 个以上的 %s", firstEntity, min, secondEntity);
            else if (max == min)
                return String.format("%s 必须包含 %d 个 %s", firstEntity, min, secondEntity);
            else
                return String.format("%s 必须包含 %d 到 %d 个 %s", firstEntity, min, max, secondEntity);
        }
        if (relation.equals(Relation.LESS_EQUAL)) {
            return String.format("%s 小于等于  %s", firstEntity, secondEntity);
        }
        if (relation.equals(Relation.LESS_THAN)) {
            return String.format("%s 小于  %s", firstEntity, secondEntity);
        }
        return "";
    }

    public String getFirstEntity() {
        return firstEntity;
    }

    public void setFirstEntity(String firstEntity) {
        this.firstEntity = firstEntity;
    }

    public String getSecondEntity() {
        return secondEntity;
    }

    public void setSecondEntity(String secondEntity) {
        this.secondEntity = secondEntity;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public boolean getNeedCheckOnline() {
        return needCheckOnline;
    }

    public void setNeedCheckOnline(boolean needCheckOnline) {
        this.needCheckOnline = needCheckOnline;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
