<?xml version="1.0"?>
<truthworthy_baseline>
  <name>装置控制基准线</name>
  <domain>装置控制领域</domain>

  <group_structure>

    <group_introduction>
      存储当前层次下的分类，type标识是条目还是分类。
      通过parent存储他们之间组织结构关系
    </group_introduction>

    <group_content>
      <content_id>1.1</content_id>
      <content_type>分类</content_type>
      <content_parent_id>NULL</content_parent_id>
      <content_description>规则</content_description>
    </group_content>
    <group_content>
      <content_id>1.2</content_id>
      <content_type>分类</content_type>
      <content_parent_id>NULL</content_parent_id>
      <content_description>需求</content_description>
    </group_content>

    <group_content>
      <content_id>2.1</content_id>
      <content_type>分类</content_type>
      <content_parent_id>1.1</content_parent_id>
      <content_description>设计建模规则</content_description>
    </group_content>
    <group_content>
      <content_id>2.2</content_id>
      <content_type>分类</content_type>
      <content_parent_id>1.1</content_parent_id>
      <content_description>代码生成规则</content_description>
    </group_content>

    
    <group_content>
      <content_id>3.1</content_id>
      <content_type>分类</content_type>
      <content_parent_id>2.1</content_parent_id>
      <content_description>层次控制结构规则</content_description>
    </group_content>
    <group_content>
      <content_id>3.2</content_id>
      <content_type>分类</content_type>
      <content_parent_id>2.1</content_parent_id>
      <content_description>构件内部规则</content_description>
    </group_content>


    <group_content>
      <content_id>4.1</content_id>
      <content_type>分类</content_type>
      <content_parent_id>3.2</content_parent_id>
      <content_description>构件粒度规则</content_description>
    </group_content>
    <group_content>
      <content_id>4.2</content_id>
      <content_type>分类</content_type>
      <content_parent_id>3.2</content_parent_id>
      <content_description>构件接口规则</content_description>
    </group_content>
    

  </group_structure>
</truthworthy_baseline>

