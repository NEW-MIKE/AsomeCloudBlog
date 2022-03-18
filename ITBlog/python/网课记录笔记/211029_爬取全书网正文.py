# 211029_爬取全书网正文
# 2021/10/29
import re  #正则表达式模块,标准库文件不需要安装
import os
import requests  #网络爬虫模块,第三方库需要安装后使用
url='http://www.quannovel.com/read/620/'  #进行爬虫的网址
req=requests.get(url)  #获取网页的内容
name=re.findall('<h2>(.*?)<i class',req.text)[0]  #获取书名
mulu=re.findall('class="name ">(.*?)</a>',req.text)
wangzhi=re.findall('<a href="(.*?).html"',req.text)
dict1={}
for i in range(len(mulu)):
    dict1[mulu[i]]=f'{url}{wangzhi[i]}.html'  #将目录和网址放入字典
count=1

if not os.path.exists(f'D:/{name}'):  #如果没有以书名命名的目录,则新建
    os.mkdir(f'D:/{name}')
for k,v in dict1.items():
    if count>5:
        break
    else:
        req=requests.get(v)  #获取正文网页的内容
        neirong=re.findall('class="page-content ">(.*?)<div class',req.text,re.S)[0]  #获取文章内容
        neirong=neirong.replace('<p>','').replace('</p>','')
        with open(f'D:/{name}/{k}.txt','w+') as file1:
            file1.write(neirong)
    print(f'第{count}章爬取完毕')
    count+=1