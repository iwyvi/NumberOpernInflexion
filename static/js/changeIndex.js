/*
IwYvI-Viring
2015.10
 */
/*
2016.1.21
修改内容
（曾经在行内加入其它词括号不匹配，
所以现在改的匹配了
新增函数
bracketMatch()
 */
/*
2016.1.28
修改内容
修正多括号匹配数目错误
 */

var indexMap = ["#","1","2","3","4","5","6","7","(",")","[","]","（","）","【","】"," "];
var toneMap = ["1","#1","2","#2","3","4","#4","5","#5","6","#6","7"];
//符号栈
var textSymbolArr = [];
//当前的音高
var tempHeight = 0;

/**
 * 转调
 * @param {string} text 输入文本
 * @param {int} mode 调式差
 */
function Change (text, mode) {
	if(mode == 0){
		return text;
	}
	//按行分割
	var line = text.split("\n");
	var result = "";
	for(var i = 0; i < line.length; i++){
		textSymbolArr.length = 0;
		tempHeight = 0;
		result += lineChange(line[i],mode);
		if(i != line.length-1){
			result+="\n";
		}
	}
	return result;
}

/**
 * 行内转调
 * @param  {string} line 每一行的字符
 * @param  {int} mode 调式差
 * @return {string}      转换后的结果
 */
function lineChange (line, mode) {
	var result = "";
	//这里强行把string当成数组
	for(var i = 0; i < line.length; i++){
		result += charChange(line[i], mode);
	}
	//处理未配对的括号
	// while(tempHeight!=0){
	// 	if(tempHeight>0){
	// 		result +="]";
	// 		tempHeight--;
	// 	}else if(tempHeight<0){
	// 		result +=")";
	// 		tempHeight++;
	// 	}
	// }
	result += bracketMatch();
	return result;
}

/**
 * 字符变换
 * @param  {string} charIndex 字符
 * @param  {int} mode      调式差
 * @return {string}           变换结果
 */
function charChange (charIndex, mode) {
	//如果不在indexMap则直接返回
	if(indexMap.indexOf(charIndex) == -1){
		charIndex = bracketMatch() + charIndex;
		return charIndex;
	}
	//如果charIndex是数字
	if(!isNaN(parseInt(charIndex))){
		var result = "";
		var index;
		var height = 0;
		//如果符号栈顶为#则charIndex=#+charIndex
		if(textSymbolArr[textSymbolArr.length-1] == "#"){
			textSymbolArr.pop();
			charIndex = "#" + charIndex;
		}
		//判断当前音高
		for(var i =0; i < textSymbolArr.length; i++){
			if(textSymbolArr[i] == "("){
				height -= 1;
			}else if(textSymbolArr[i] == "["){
				height += 1;
			}
		}
		//修正#4和#7
		if(charIndex == "#3"){
			charIndex = "4";
		}else if(charIndex == "#7"){
			height +=1;
			charIndex = "1";
		}
		//获取当前charIndex的对应index值
		for(var i = 0; i < toneMap.length; i++){
			if(toneMap[i] == charIndex){
				index = i;
				break;
			}
		}
		//获取转调后的index
		index = index + mode;
		//处理转调后的音高
		if(index >= toneMap.length){
			index = index%toneMap.length;
			height+=1;
		}else if(index< 0){
			index = toneMap.length + index;
			height-=1;
		}
		//处理音高的括号问题
		if(height != tempHeight){
			if(height > tempHeight){
				for(var i = tempHeight + 1; i <= height; i++){
					if(i<0){
						result += ")";
					}else if(i>0){
						result +="[";
					}else{
                        if (tempHeight == -1){
                            result += ")";
                        }else if(tempHeight == 1){
                            result += "[";
                        }
                    }
				}
			}else if(height < tempHeight){
				for(var i = tempHeight - 1; i >= height; i--){
					if(i<0){
						result += "(";
					}else if(i>0){
						result +="]";
					}else{
                        if (tempHeight == -1){
                            result += "(";
                        }else if(tempHeight == 1){
                            result += "]";
                        }
                    }
				}
			}
			tempHeight = height;
		}

		result += toneMap[index];
		return result;
	}
	//处理符号
	switch(charIndex){
	case "#":
		if(textSymbolArr[textSymbolArr.length-1] != "#"){
			textSymbolArr.push("#");
		}
		break;
	case "(":
	case "（":
		if(textSymbolArr[textSymbolArr.length-1] == "#"){
			textSymbolArr.length = textSymbolArr.length-1;
		}
		textSymbolArr.push("(");
		break;
	case ")":
	case "）":
		for (var i = textSymbolArr.length - 1; i >= 0; i--) {
			if(textSymbolArr[i] == "("){
				textSymbolArr.length = i;
				break;
			}
		};
		break;
	case "[":
	case "【":
		if(textSymbolArr[textSymbolArr.length-1] == "#"){
			textSymbolArr.length = textSymbolArr.length-1;
		}
		textSymbolArr.push("[");
		break;
	case "]":
	case "】":
		for (var i = textSymbolArr.length - 1; i >= 0; i--) {
			if(textSymbolArr[i] == "["){
				textSymbolArr.length = i;
				break;
			}
		};
		break;
	//default是处理空格什么的
	default:
		return bracketMatch() + charIndex;
	}
	return "";
}

/**
 * 匹配括号（用在整句结束后）
 * @return {String} 被匹配的括号
 */
function bracketMatch () {
	var result = "";
	while(tempHeight!=0){
		if(tempHeight>0){
			result +="]";
			tempHeight--;
		}else if(tempHeight<0){
			result +=")";
			tempHeight++;
		}
	}
	return result;
}

