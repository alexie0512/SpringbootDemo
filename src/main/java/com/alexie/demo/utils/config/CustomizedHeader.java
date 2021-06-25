package com.alexie.demo.utils.config;

/**
 * 自定义请求头
 *
 * @author Alexie on 2021/6/9
 * @ClassName CustomizedHeader
 * @Description TODO
 * @Version 1.0
 */

public interface CustomizedHeader {


     /*********************************
      *********************************
      *********************************/

     String EXPIRES="SHORT";
     String USER_ID="1";



     /**********************************
      ***********  测试环境-租户T2   *****
      *********************************/

     String TENANT_T2="t2";
     String USER_NAME_T2="tzvirtual1@tezign.com";
     String PWD_T2="qq111111";


     /*********************************
      ********* 测试环境-租户T13  ********
      *********************************/
     String TENANT_T13="t13";
     String USER_NAME_T13="admin@vms-t13.tezign.com";
     String PWD_T13="Ul02n9a2";



     /*********************************
      *********** KIBANA VERSION *******
      *********************************/

     String KIBANA_VERSION="5.6.16";
     String KIBANA_USER="elastic";
     String KIBANA_PWD="CwWkTuF1AY14HA3OTo66";


}
