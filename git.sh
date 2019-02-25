#! /bin/bash
echo "step1---git add .添加文件\n"
git add .
echo "step2---git commit -m 添加推送\n"
git commit -m "$1"
echo "step2---git push推送\n"
git push
echo "Success\n";