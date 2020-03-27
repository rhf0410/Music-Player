package com.example.kmp;

public class KMP 
{
	/**
	 * ��ȡ�ַ����Ĳ���ֵ
	 * @param ref ģʽ��
	 * @return �����ַ����Ĳ���ֵ
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
     * �ж��ַ����Ƿ�ƥ��
     * @param ref1 ����
     * @param ref2 ƥ�䴮
     * @return ��ƥ�䷵��true,��ƥ�䷵��false
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
