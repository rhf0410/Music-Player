package com.example.kmp;

public class KMP 
{
	/**
	 * 获取字符串的参照值
	 * @param ref 模式串
	 * @return 返回字符串的参照值
	 */
    public static int[] get_next(char []ref)
    {
    	int []next=new int[ref.length];
    	next[0]=-1;
    	int i=0,j=-1;
    	while(i<ref.length-1)
    	{
    		if(j==-1||ref[i]==ref[j])
    		{
    			i++;
    			j++;
    			if(ref[i]!=ref[j])
    			{
    				next[i]=j;
    			}
    			else
    			{
    				next[i]=next[j];
    			}
    		}
    		else
    		{
    			j=next[j];
    		}
    	}
    	return next;
    }
    
    /**
     * 判断字符串是否匹配
     * @param ref1 主串
     * @param ref2 匹配串
     * @return 若匹配返回true,不匹配返回false
     */
    public static boolean Kmp_Matching(char[] ref1,char[] ref2)
    {
    	boolean flag=false;
    	int []next=get_next(ref2);
    	int i=0,j=0;
    	while(i<=ref1.length-1&&j<=ref2.length-1)
    	{
    		if(j==-1||ref1[i]==ref2[j])
    		{
    			i++;
    			j++;
    		}
    		else
    		{
    			j=next[j];
    		}
    	}
    	if(j>=ref2.length)
    		flag=true;	
    	return flag;
    }
}
