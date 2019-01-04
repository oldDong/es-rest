分析器

1、字符过滤器

    字符串按顺序通过每个字符过滤器，他们的任务是在分词前整理字符串。
    一个字符过滤器可以用来去掉HTML，或者将 & 转化成 and。

2、分词器

    字符串被分词器分为单个的词条。
    一个简单的分词器遇到空格和标点的时候，可能会将文本拆分成词条。
    
3、Token过滤器

    词条按顺序通过每个token过滤器。
    可能会改变词条（例如，小写化Quick）、删除词条（例如，像a、and、the等无用词），或者增加词条（例如，像jump和leap这种同义词）
    

4、内置分析器
    
    例子："中华人民共和国国歌"

    1、标准分析器
    ES默认使用的分析器。
    根据Unicode联盟定义的单词边界划分文本，删除绝大部分标点。最后，将词条小写。
    curl -XGET 'http://hadoop1:9200/_analyze?pretty' -d '{"analyzer" : "standard", "text":"中华人民共和国国歌"}'
    {
      "tokens" : [
        {
          "token" : "中",
          "start_offset" : 0,
          "end_offset" : 1,
          "type" : "<IDEOGRAPHIC>",
          "position" : 0
        },
        {
          "token" : "华",
          "start_offset" : 1,
          "end_offset" : 2,
          "type" : "<IDEOGRAPHIC>",
          "position" : 1
        },
        {
          "token" : "人",
          "start_offset" : 2,
          "end_offset" : 3,
          "type" : "<IDEOGRAPHIC>",
          "position" : 2
        },
        {
          "token" : "民",
          "start_offset" : 3,
          "end_offset" : 4,
          "type" : "<IDEOGRAPHIC>",
          "position" : 3
        },
        {
          "token" : "共",
          "start_offset" : 4,
          "end_offset" : 5,
          "type" : "<IDEOGRAPHIC>",
          "position" : 4
        },
        {
          "token" : "和",
          "start_offset" : 5,
          "end_offset" : 6,
          "type" : "<IDEOGRAPHIC>",
          "position" : 5
        },
        {
          "token" : "国",
          "start_offset" : 6,
          "end_offset" : 7,
          "type" : "<IDEOGRAPHIC>",
          "position" : 6
        },
        {
          "token" : "国",
          "start_offset" : 7,
          "end_offset" : 8,
          "type" : "<IDEOGRAPHIC>",
          "position" : 7
        },
        {
          "token" : "歌",
          "start_offset" : 8,
          "end_offset" : 9,
          "type" : "<IDEOGRAPHIC>",
          "position" : 8
        }
      ]
    }

    2、简单分析器
    在任何不是字母的地方分隔文本，将词条小写。
    curl -XGET 'http://hadoop1:9200/_analyze?pretty' -d '{"analyzer" : "simple", "text":"中华人民共和国国歌"}'
    {
      "tokens" : [
        {
          "token" : "中华人民共和国国歌",
          "start_offset" : 0,
          "end_offset" : 9,
          "type" : "word",
          "position" : 0
        }
      ]
    }

    3、空格分析器
    在空格的地方划分文本。
    curl -XGET 'http://hadoop1:9200/_analyze?pretty' -d '{"analyzer" : "whitespace", "text":"中华人民共和国国歌"}'
    {
      "tokens" : [
        {
          "token" : "中华人民共和国国歌",
          "start_offset" : 0,
          "end_offset" : 9,
          "type" : "word",
          "position" : 0
        }
      ]
    }


    4、语言分析器
    英文分词器
    自带一组英语无用词（and/the/to/a...）
    curl -XGET 'http://hadoop1:9200/_analyze?pretty' -d '{"analyzer" : "english", "text":"中华人民共和国国歌"}'
    {
      "tokens" : [
        {
          "token" : "中",
          "start_offset" : 0,
          "end_offset" : 1,
          "type" : "<IDEOGRAPHIC>",
          "position" : 0
        },
        {
          "token" : "华",
          "start_offset" : 1,
          "end_offset" : 2,
          "type" : "<IDEOGRAPHIC>",
          "position" : 1
        },
        {
          "token" : "人",
          "start_offset" : 2,
          "end_offset" : 3,
          "type" : "<IDEOGRAPHIC>",
          "position" : 2
        },
        {
          "token" : "民",
          "start_offset" : 3,
          "end_offset" : 4,
          "type" : "<IDEOGRAPHIC>",
          "position" : 3
        },
        {
          "token" : "共",
          "start_offset" : 4,
          "end_offset" : 5,
          "type" : "<IDEOGRAPHIC>",
          "position" : 4
        },
        {
          "token" : "和",
          "start_offset" : 5,
          "end_offset" : 6,
          "type" : "<IDEOGRAPHIC>",
          "position" : 5
        },
        {
          "token" : "国",
          "start_offset" : 6,
          "end_offset" : 7,
          "type" : "<IDEOGRAPHIC>",
          "position" : 6
        },
        {
          "token" : "国",
          "start_offset" : 7,
          "end_offset" : 8,
          "type" : "<IDEOGRAPHIC>",
          "position" : 7
        },
        {
          "token" : "歌",
          "start_offset" : 8,
          "end_offset" : 9,
          "type" : "<IDEOGRAPHIC>",
          "position" : 8
        }
      ]
    }

    中文分词器（ik_max_word、ik_smart）
    
    curl -XGET 'http://hadoop1:9200/_analyze?pretty' -d '{"analyzer" : "ik_smart", "text":"中华人民共和国国歌"}'
    result
    
    {
      "tokens" : [
        {
          "token" : "中华人民共和国",
          "start_offset" : 0,
          "end_offset" : 7,
          "type" : "CN_WORD",
          "position" : 0
        },
        {
          "token" : "国歌",
          "start_offset" : 7,
          "end_offset" : 9,
          "type" : "CN_WORD",
          "position" : 1
        }
      ]
    }
    
    curl -XGET 'http://hadoop1:9200/_analyze?pretty' -d '{"analyzer" : "ik_max_word", "text":"中华人民共和国国歌"}'
    
    {
      "tokens" : [
        {
          "token" : "中华人民共和国",
          "start_offset" : 0,
          "end_offset" : 7,
          "type" : "CN_WORD",
          "position" : 0
        },
        {
          "token" : "中华人民",
          "start_offset" : 0,
          "end_offset" : 4,
          "type" : "CN_WORD",
          "position" : 1
        },
        {
          "token" : "中华",
          "start_offset" : 0,
          "end_offset" : 2,
          "type" : "CN_WORD",
          "position" : 2
        },
        {
          "token" : "华人",
          "start_offset" : 1,
          "end_offset" : 3,
          "type" : "CN_WORD",
          "position" : 3
        },
        {
          "token" : "人民共和国",
          "start_offset" : 2,
          "end_offset" : 7,
          "type" : "CN_WORD",
          "position" : 4
        },
        {
          "token" : "人民",
          "start_offset" : 2,
          "end_offset" : 4,
          "type" : "CN_WORD",
          "position" : 5
        },
        {
          "token" : "共和国",
          "start_offset" : 4,
          "end_offset" : 7,
          "type" : "CN_WORD",
          "position" : 6
        },
        {
          "token" : "共和",
          "start_offset" : 4,
          "end_offset" : 6,
          "type" : "CN_WORD",
          "position" : 7
        },
        {
          "token" : "国",
          "start_offset" : 6,
          "end_offset" : 7,
          "type" : "CN_CHAR",
          "position" : 8
        },
        {
          "token" : "国歌",
          "start_offset" : 7,
          "end_offset" : 9,
          "type" : "CN_WORD",
          "position" : 9
        }
      ]
    }
    
    5、关键词分析器
    接受任何给定的文本，并输出完全相同的文本作为一个单词
    curl -XGET 'http://hadoop1:9200/_analyze?pretty' -d '{"analyzer" : "keyword", "text":"中华人民共和国国歌"}'
    {
      "tokens" : [
        {
          "token" : "中华人民共和国国歌",
          "start_offset" : 0,
          "end_offset" : 9,
          "type" : "word",
          "position" : 0
        }
      ]
    }
    
映射

    curl -XGET 'http://hadoop1:9200/crm_opr_kp_update/_mapping/crm_opr_kp_update?pretty'
    
    {
      "crm_opr_kp_update" : {
        "mappings" : {
          "crm_opr_kp_update" : {
            "include_in_all" : false,
            "properties" : {
              "account" : {
                "type" : "integer"
              },
              "city" : {
                "type" : "text",
                "analyzer" : "keyword"
              },
              "depttitles" : {
                "type" : "text",
                "analyzer" : "ik_max_word"
              },
              "firstclass" : {
                "type" : "text",
                "analyzer" : "keyword"
              },
              "lastuv" : {
                "type" : "integer"
              },
              "orgid" : {
                "type" : "long"
              },
              "orgname" : {
                "type" : "text",
                "analyzer" : "ik_max_word"
              },
              "qdset" : {
                "type" : "text",
                "analyzer" : "keyword"
              },
              "qdusercount" : {
                "type" : "integer"
              },
              "secondclass" : {
                "type" : "text",
                "analyzer" : "keyword"
              },
              "tags" : {
                "type" : "text",
                "analyzer" : "whitespace"
              },
              "type" : {
                "type" : "text",
                "analyzer" : "keyword"
              },
              "uid" : {
                "type" : "long"
              },
              "usercount" : {
                "type" : "integer"
              },
              "username" : {
                "type" : "text",
                "analyzer" : "keyword"
              },
              "uvcount" : {
                "type" : "integer"
              },
              "uvrate" : {
                "type" : "float"
              }
            }
          }
        }
      }
    }
    
    字符串： string、text
    整数：byte、short、integer、long
    浮点数：float、double
    布尔型：boolean
    日期：date
    
    创建一个新索引，并制定分词器和映射
    
    curl -XPUT 'http://hadoop1:9200/gb' -d '{"mappings" : {"tweet" : {"properties" : {"tweet" : {"type":"string", "analyzer" : "english"},"date" : {"type" : "date"}, "name" : {"type" : "string"}, "user_id" : {"type" : "long"}}}}}'
    查看创建的索引的映射````
    curl -XGET 'http://hadoop1:9200/gb/_mapping/tweet?pretty'

    
    