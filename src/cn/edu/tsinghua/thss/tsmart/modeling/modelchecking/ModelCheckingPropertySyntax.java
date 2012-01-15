/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.modelchecking;

/**
 * @author 张华枫
 *
 */
public class ModelCheckingPropertySyntax {
	public Boolean propertyParser(String property)
	{
//		Boolean result = true;
		
		property = property.trim();
		
//		if (property.startsWith("INVARSPEC"))
//		{
//			property = property.substring("INVARSPEC".length());
//			return parse(property, false);	//false for no expression in property's left
//		}			
//		else
//		{
//			result = false;
//		}
		
		
		return parse(property, false);
	}
	
	private Boolean parse(String property, Boolean isLeftExpr)
	{
		if(property.length() == 0 && isLeftExpr == true)
		{
			return true;
		}
		
		Boolean result = false;
		
		char [] p = property.toCharArray();
		//去除掉前缀空格
		int i = 0;
		while( i < p.length && p[i] == ' ')
		{
			i++;
			continue;
		}
		
		if(p[i] == '(' && isLeftExpr == false)
		{
			// search for right parenthesis
			System.out.println("parenthesis");

			//search for paired parenthesis
			int j = i + 1;
			int pcount = 0;
			Boolean find = false;
			for(;j < p.length;j++)
			{
				switch(p[j])
				{
				case '(':
					pcount++;
					break;
				case ')':
					if(pcount == 0)
					{
						find = true;
					}
					else
					{
						pcount--;
					}
					break;
				default:
					continue;
				}
				
				if(find == true)
					break;
			}
			
			if(find == false | parse(property.substring(i + 1, j), false) == false)
				return false;
			else
				return parse(property.substring(j + 1), true);
		}
		if(p[i] == '!' && isLeftExpr == false)
		{
			// not relation
			System.out.println("not");

			result = parse(property.substring(i + 1), false);
		}
		if(p[i] == '&' && isLeftExpr == true)
		{
			// and relation
			System.out.println("and");

			result = parse(property.substring(i + 1), false);
		}
		if(p[i] == '|' && isLeftExpr == true)
		{
			// or relation
			System.out.println("or");
			
			result = parse(property.substring(i + 1), false);
		}
		if(p[i] == '=' && isLeftExpr == true)
		{
			// equal relation
			System.out.println("equal");
			
			if(i < p.length - 1 && p[i+1] == '=')
				result = parse(property.substring(i + 2), false);
			else
				result = false;
//			System.out.println("==");			
		}
		if(((p[i]<= 'Z' && p[i] >='A') || (p[i]<= 'z' && p[i] >='a'))  && isLeftExpr == false)
		{
			System.out.println("id");

			while(i < p.length && 
					((p[i]<= 'Z' && p[i] >='A') || 
							(p[i]<= 'z' && p[i] >='a') || 
							p[i] == '.' ||
							p[i] == '_' ||
							(p[i] <= '9' && p[i] >= '0')))
			{
				i++;
			}
			//左边是个id，因此是个expression。
			result = parse(property.substring(i), true);
		}
		return result;		
	}
}
