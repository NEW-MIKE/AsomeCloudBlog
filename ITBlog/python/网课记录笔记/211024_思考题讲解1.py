# 思考题讲解1
#第三次课思考题1
#根据身份证号判断拥有者的性别
# id='32010419991209089X'
# if int(id[-2])%2==1:
#     print('男性')
# else:
#     print('女性')

#第三次课思考题2
# str1='A girl come in, the name is Jack, level 955;'
# str2='A old lady come in, the name is Mary, level 94454'
#
# def getName(srcStr):
#     str1_new = srcStr.split(',')[1]
#     srcStr = str1_new.split(' ')[-1]
#     return srcStr
# print(getName(str1))

#第四次课思考题1
# Jack Green ,   21  ;  Mike Mos, 9;

# Jack Green          :21;
# Mike Mos            :09;

#第一步,拆分字符串,以分号作为分隔符
#第二步,把姓名和年龄分开,以逗号作为分隔符
#第三步,按照题目要求的格式进行打印
# str3=input('请输入一个字符串:  ')
# list3=str3.split(';')
# for one in list3:
#     if one != '':  #只处理非空的值
#         # name=one.split(',')[0]
#         # age = one.split(',')[1]
#         name,age=one.split(',')
#         name=name.strip()
#         age=age.strip()
#         print(f'{name:20}:{age:>02};')

#第四次课思考题2
#第一步,将文件类型提取出来
#①以行为单位,每次取一行  ②以\t作为分隔符,分隔各个段落  ③以"."作为分隔符,取得文件类型
#第二步,将文件大小提取出来
#第三步,将相同类型的文件进行累加

log = '''
f20180111014341/i_51a7hC3W.jpeg	169472	FrITJxleSP7wUD-MWw-phL_KP6Eu	15156063244230469	image/jpeg	0	
f20180111014341/j_R0Hpl4EG.json	1036	ForGzwzV3e-uR3_UzvppJs1VgfQG	15156064773253144	application/json	0	
f20180111020739/i_0TDKs0rD.jpeg	169472	FrITJxleSP7wUD-MWw-phL_KP6Eu	15156076847077556	image/jpeg	0	
f20180111020739/j_JFO6xiir.json	1040	FmUhTchdLOd7LBoE8OXzPLDKcW60	15156077904192983	application/json	0	
f20180111090619/i_1BwNksbL.jpg	49634	FtXBGmipcDha-67WQgGQR5shEBu2	15156329458714950	image/jpeg	0	
f20180111090619/i_3BKlsRaZ.jpg	30152	FoWfMSuqz4TEQl5FT-FY5wqu5NGf	15156330575626044	image/jpeg	0	
f20180111090619/i_5XboXSKh.jpg	40238	Fl84WaBWThHovIBsQaNFoIaPZcWh	15156329453409855	image/jpeg	0	
f20180111090619/i_6DiYSBKp.jpg	74017	FrYG3icChRmFGnWQK6rYxa88KuQI	15156329461803290	image/jpeg	0	
f20180111090619/i_76zaF2IM.jpg	38437	Fui8g5OrJh0GQqZzT9wtepfq99lJ	15156334738356648	image/jpeg	0	
f20180111090619/i_B6TFYjks.jpg	37953	FleWqlK2W1ZmEgAatAEcm1gpR0kC	15156329464034474	image/jpeg	0	
f20180111090619/i_N9eITqj3.jpg	38437	Fui8g5OrJh0GQqZzT9wtepfq99lJ	15156330419595764	image/jpeg	0	
f20180111090619/i_QTSNWmA6.jpg	37953	FleWqlK2W1ZmEgAatAEcm1gpR0kC	15156333104224056	image/jpeg	0	
f20180111090619/i_XdHcAfh1.jpg	56479	FjLQIQ3GxSEHDfu6tRcMylK1MZ05	15156334227270309	image/jpeg	0	
f20180111090619/i_Xyy723MU.jpg	50076	FsfZpQzqu084RUw5NPYW9-Yfam_R	15156334229987458	image/jpeg	0	
f20180111090619/i_d8Go0EOv.jpg	30152	FoWfMSuqz4TEQl5FT-FY5wqu5NGf	15156334736228515	image/jpeg	0	
f20180111090619/i_diuHmX53.jpg	40591	FuTx1pw4idbKnV5MSvNGxCA5L470	15156333878320713	image/jpeg	0	
f20180111090619/i_qQKzheSH.jpg	55858	Fj0A3i8V7fzzOiPQFL79ao15hkN9	15156329456666591	image/jpeg	0	
f20180111090619/i_rHL5SYk8.jpg	40238	Fl84WaBWThHovIBsQaNFoIaPZcWh	15156336509742181	image/jpeg	0	
f20180111090619/i_xZmQxUbz.jpg	40238	Fl84WaBWThHovIBsQaNFoIaPZcWh	15156333240603466	image/jpeg	0	
f20180111090619/i_zBDNgXDv.jpeg	73616	FlgNwq8lypgsxrWs_ksrS_x47SQV	15156334232887875	image/jpeg	0	
f20180111090619/j_4mxbEiVh.json	2990	Fpq-3yl3Yr1CadNrJVSDnpeRhQtT	15156331445226898	application/json	0	
f20180111090619/j_i1K74768.json	3042	Fl5PpDw1TsZXMuhoq1RUrOeGZ6br	15156335067090003	application/json	0	
f20180111095839/i_Q7KMKeda.png	518522	Fl-yB1_ruL2uxZN9k7DjB62h9dYH	15156359599713253	image/png	0	
f20180111095839/j_5DpqHolV.json	184	FoYvi7cmSrzuVjUgCRzW5kU95SVo	15156359719719064	application/json	0	
f20180111100442/i_No8kToIV.jpg	48975	Fu1cw3f--5Vpz9kLGeJfvljhCtyZ	15156364349642377	image/jpeg	0	
f20180111100442/i_P1bkvSeg.jpg	68200	FvYe8vi46TjUKhEy_UwDqLhO6ZsW	15156363800690634	image/jpeg	0	
f20180111100442/i_T1AulKcD.jpg	52641	Fj2YzvdC1n_1sF93ZZgrhF3OzOeY	15156364021186365	image/jpeg	0	
f20180111100442/i_X8d8BN07.jpg	50770	FivwidMiHbogw77lqgkIKrgmF3eA	15156363969737156	image/jpeg	0	
f20180111100442/i_g0wtOsCX.jpg	76656	Fmtixx0mP9CAUTNosjLuYQHL6k0P	15156363448222155	image/jpeg	0	
f20180111100442/i_h5OT9324.jpg	72672	FvbIqPLTh2cQHTIBv2akUfahZa_Z	15156364401354652	image/jpeg	0	
f20180111100442/i_he8iLYI6.jpg	49399	FjeJvwjwhU-hKZsq66UoBg9_tEJs	15156363907932480	image/jpeg	0	
f20180111100442/i_kg29t7Pp.jpg	76293	FuYj__sSeEN7AsXMbxO24Z8Suh8d	15156364156384686	image/jpeg	0	
f20180111100442/i_oz1YoBI1.jpg	75620	FkY3xsUMwOI01zgoH1iXXgiQeq6I	15156364089112904	image/jpeg	0	
f20180111100442/i_xrOT98on.jpg	50021	Fql7ookM1Rc6V7VairKAfnKe-o9w	15156363856357316	image/jpeg	0	
f20180111135114/i_Zqt8Tmoe.png	161629	FlELw59_mV3VqDBLyu1BKN4fIWnx	15156500155209863	image/png	0	
f20180111135114/j_uhHoMXKq.json	159	FrypljwAr2LgoLAePBNTUYTUAgDt	15156500200488238	application/json	0	
f20180111142119/i_s83iZ2GR.png	92278	Fns8tdh3JCkRmfE_COYEu4o8w03E	15156517082371259	image/png	0	
f20180111142119/j_0g45JRth.json	159	Fq1rFwdRguYRXrp61nGZ5TsUG1V-	15156517143375596	application/json	0	
f20180111144306/i_yE5TC84E.png	139230	Fjf61ymabEnEvnr5ZMHFjXGCrYlP	15156530038824150	image/png	0	
f20180111144306/j_OF4WVtSH.json	159	FqwkKcxfo8jd0jFUyuH4X2CrnE9q	15156530083419530	application/json	0	
f20180111150230/i_KtnER4g3.png	120044	FuwOWdrqzcr2-UScem-LzEMgMezs	15156541734892258	image/png	0	
f20180111150230/j_xMSUEejY.json	158	FjJr_4deMqFphGaptm-2Pa6wwRP2	15156541771989216	application/json	0	
f20180111151741/i_JuSWztB3.jpg	92506	FrIjRevHSi6xv4-NQa2wrHu5a1zQ	15156550875370965	image/jpeg	0	
f20180111153550/i_9wWzVenl.gif	769872	FvslKY9JUaCQm-lu02E34tvAP_oG	15156561674621628	image/gif	0	
'''
dict1={}
line_list=log.split('\n')  #以行为单位,得到一个列表,每一行是一个元素
for one in line_list:
    if one != '':  #只处理非空行
        one1=one.split('\t')[0]   #取得含有文件类型的那一段
        file_type=one1.split('.')[1]  #取得文件类型
        file_size=int(one.split('\t')[1])  #取得文件大小,转为int型
        if file_type not in dict1:  #如果字典中没有这个文件类型,则新增文件类型和文件大小
            dict1[file_type]=file_size
        else:
            dict1[file_type]+=file_size  #将文件大小进行累加
print(dict1)