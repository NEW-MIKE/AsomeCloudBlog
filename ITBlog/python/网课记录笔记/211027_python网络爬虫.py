# 211027_python网络爬虫
import re  #正则表达式模块,标准库文件不需要安装
import requests  #网络爬虫模块,第三方库需要安装后使用
# url='http://www.quannovel.com/read/620/'  #进行爬虫的网址
# req=requests.get(url)  #获取网页的内容
# mulu=re.findall('class="name ">(.*?)</a>',req.text)
# for one in mulu:
#     print(one)
# wangzhi=re.findall('<a href="(.*?).html"',req.text)
# for one in wangzhi:
#     print(f'{url}{one}.html')
# dict1={}

# for i in range(len(mulu)):
#     dict1[mulu[i]]=f'{url}{wangzhi[i]}.html'  #将目录和网址放入字典
# for k,v in dict1.items():
#     print(k,v)

#统计10000以内有多少个含有9的数字
# count=0
# for i in range(1,10000):
#     if '9' in str(i):
#         count+=1
# print(count)

#列表推导式的方式实现
print(len([i for i in range(1,10000) if '9' in str(i)]))