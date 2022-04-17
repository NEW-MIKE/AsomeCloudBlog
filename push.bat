:: 输入提交内容
set /p commit_msg=Please input commit message:
:: 添加
git add .
:: 提交
git commit -m "%commit_msg%"
:: 推送到远程副分支  master
git push origin master
:: 停顿 
pause