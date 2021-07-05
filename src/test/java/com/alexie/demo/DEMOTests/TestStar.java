package com.alexie.demo.DEMOTests;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.alexie.demo.utils.FileUtils;

/**
 * ceshi
 *
 * @author Alexie on 2021/6/4
 * @ClassName TestStar
 * @Description TODO
 * @Version 1.0
 */
//
@Slf4j
//@ContextConfiguration()
//@ExtendWith(SpringExtension.class)
//@Component
//@SpringBootTest(classes = DemoApplication.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class TestStar{

    private Integer id = 0;

    public void getHTML() throws Exception {

        StringBuilder sb = new StringBuilder();
        Document doc = Jsoup.connect("http://www.manmankan.com/dy2013/mingxing/neidi/").get();
        Elements lisDIV = doc.getElementsByAttributeValue("class","show");
        for(Element text: lisDIV){
            //System.out.println(text.attr("title"));
            sb.append((++id) + "： " +text.attr("title")+"\n");
        }
        FileUtils.writeStrtoFile(sb.toString(),"src/test/output","starlist.txt");
    }

//
//    @Test
//    public void LoadFromYaml(){   //String filPath
//        Yaml yml =new Yaml(new Constructor(ESSearchDto.class));
//        InputStream inputStream = this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("/searchfiles/ESSearch.yaml");
//        ESSearchDto esSearchDto = yml.load(inputStream);
//        System.out.println(esSearchDto);
//    }

//    @Test
//    public void toYaml(){
//        ESSearchDto esSearchDto = new ESSearchDto();
//        esSearchDto.
//    }



}

