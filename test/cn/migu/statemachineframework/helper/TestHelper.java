package cn.migu.statemachineframework.helper;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

/**
 * ¸¨Öú²âÊÔ½á¹ûÅÐ¶Ï
 */
public class TestHelper
{

    public static void assertStringListEqual(List<String> exp, List<String> rst)
    {
        try
        {
            do
            {
                if (exp.size() != rst.size())
                {
                    System.out.println("exp size:" + exp.size() + " result size:" + rst.size());
                    break;
                }
                Iterator<String> it1 = exp.iterator();
                Iterator<String> it2 = rst.iterator();
                boolean foundError = false;
                while (it1.hasNext())
                {
                    String expOne = it1.next();
                    String rstOne = it2.next();
                    if (!expOne.equals(rstOne))
                    {
                        foundError = true;
                        break;
                    }
                }
                if (foundError)
                {
                    break;
                }
                return;
            }
            while (false);
            System.out.println("exp:" + exp);
            System.out.println("rst:" + rst);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Assert.fail();
    }

}
