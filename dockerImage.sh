# Initialization step

red='\033[0;31m'
green='\033[0;32m'
yellow='\033[0;33m'
plain='\033[0m'

paas_git_repository="/home/repository/yesmywine"
mall_git_repository="/home/repository/yesmywine_ms"

platform=(
	paas
	mall
)

paas=(
	paas-goods
	paas-inventory
	paas-user
	paas-logistics
	paas-sms
	paas-email
	paas-dic
	all
)

mall=(
	mall-goods
	mall-cart
	mall-cms
	mall-evaluation
	mall-fileupload
	mall-inventory
	mall-logistics
	mall-pay
	mall-push
	mall-sso
	mall-user
	mall-orders
	mall-activity
	all
)


deploy_microservice() {
	platform_select
	deploy_prepare
}

undeploy_microservice() {
	echo "undeploy?"
}

platform_select() {
	# if ! deploy_check; then
	#     echo -e "${red}Error:${plain} Your OS is not supported to run it!"
	#     echo "Please change to CentOS 6+ and try again."
	#     exit 1
	# fi

	clear
	while true; do
		echo "Which platform you'd select:"
		for ((i = 1; i <= ${#platform[@]}; i++)); do
			hint="${platform[$i - 1]}"
			echo -e "${green}${i}${plain}) ${hint}"
		done
		read -p "Please enter a number (Default ${platform[0]}):" platform_selected
		[ -z "${platform_selected}" ] && platform_selected="1"
		case "${platform_selected}" in
		1 | 2)
			echo
			echo "You choose = ${platform[${platform_selected} - 1]}"
			echo
			break
			;;
		*)
			echo -e "${red}Error:${plain} Please only enter a number [1-2]"
			;;
		esac
	done
}

deploy_prepare() {
	if [[ "${platform_selected}" == "1" ]]; then
		# repository_check ${paas_git_repository}
		microservice_select "${paas[*]}"
		config_paas_microservice
	elif [ "${platform_selected}" == "2" ]; then
		# repository_check ${mall_git_repository}
		microservice_select "${mall[*]}"
		config_mall_microservice
	fi

	echo
	echo "Press any key to start...or Press Ctrl+C to cancel"
	char=$(get_char)
}

get_char() {
	SAVEDSTTY=$(stty -g)
	stty -echo
	stty cbreak
	dd if=/dev/tty bs=1 count=1 2>/dev/null
	stty -raw
	stty echo
	stty $SAVEDSTTY
}


whetherUpload() {
	while true; do
		read -p "Whether upload to remote server(Please enter y or n,Default y):" sign
		case $sign in
			[y])
				echo "Yes"
				break
				;;

			[n])
				echo "Complete!"
				exit
				;;

			*)
			echo "Invalid input..."
			break
			;;
		esac
	done
}

microservice_select() {
	while true; do
		echo -e "Please select microservice for ${platform[${platform_selected} - 1]}:"

		microservice_group=($1)

		for ((i = 1; i <= ${#microservice_group[@]}; i++)); do
			hint="${microservice_group[$i - 1]}"
			echo -e "${green}${i}${plain}) ${hint}"
		done
		read -p "Which microservice you'd select(Default: ${microservice_group[0]}):" microservice_selected
		[ -z "$microservice_selected" ] && microservice_selected=1
		expr ${microservice_selected} + 1 &>/dev/null
		if [ $? -ne 0 ]; then
			echo -e "[${red}Error${plain}] Input error, please input a number"
			continue
		fi
		if [[ "$microservice_selected" -lt 1 || "$microservice_selected" -gt ${#microservice_group[@]} ]]; then
			echo -e "[${red}Error${plain}] Input error, please input a number between 1 and ${#microservice_group[@]}"
			continue
		fi
		microservice=${microservice_group[$microservice_selected - 1]}
		echo 
		echo "microservice = ${microservice}"
		echo
		break
	done
}

config_paas_microservice() {

	if [[ "${microservice_selected}" == "1" ]]; then
		echo "Change ${paas_git_repository}/goods"
		if [ -d "${paas_git_repository}/goods" ]; then
			cd ${paas_git_repository}/goods
			git pull
			if [[ $? -eq 0 ]];then
				sh runDocker.sh
				
				whetherUpload
			
				echo "Change /home/repository/images"
				if [ -d "/home/repository/images" ]; then
					cd /home/repository/images				
				else
					echo "No directory,Create it"
					mkdir -p /home/repository/images
					cd /home/repository/images
				fi
				echo "Export image"	
				docker save -o paas-goods.tar paas-goods
				echo "Upload image to remote server"
				read -p "Please enter remote ip:" ip
								
				scp /home/repository/images/paas-goods.tar ${ip}:/home/repository/images 
				
				if [[ $? -eq 0 ]];then
					echo "Change machine"
					ssh ${ip}
				else
					"Remote server /home/repository/images not found,Please create it first"	
				fi
			else
				echo -e "Error:Uninstall GIT!"
			fi
					else
			echo -e "${red}Error:${plain} ${1} directory not found."
			exit 1
		fi
	elif [ "${microservice_selected}" == "2" ]; then
		echo "Change ${paas_git_repository}/inventory"
		if [ -d "${paas_git_repository}/inventory" ]; then
			cd ${paas_git_repository}/goods
			git pull
			if [[ $? -eq 0 ]];then
				sh runDocker.sh
						
				whetherUpload									
				
				echo "Change /home/repository/images"
				if [ -d "/home/repository/images" ]; then
						cd /home/repository/images
				else
						echo "No directory,Create it"
						mkdir -p /home/repository/images
						cd /home/repository/images
				fi
				echo "Export image"     
				docker save -o paas-inventory.tar paas-inventory
				echo "Upload image to remote server"
				read -p "Please enter remote ip:" ip
				scp /home/repository/images/paas-inventory.tar ${ip}:/home/repository/images

				if [[ $? -eq 0 ]];then
						echo "Change machine"
						ssh ${ip}
				else
						"Remote server /home/repository/images not found,Please create it first"
				fi
			else
				echo -e "Error:Uninstall GIT!"
			fi
		else
			echo -e "${red}Error:${plain} ${1} directory not found."
			exit 1
		fi
	elif [ "${microservice_selected}" == "3" ]; then
		echo "Change ${paas_git_repository}/user"
		if [ -d "${paas_git_repository}/user" ]; then
			cd ${paas_git_repository}/user
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o paas-user.tar paas-user
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip

					scp /home/repository/images/paas-user.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
			echo -e "${red}Error:${plain} ${1} directory not found."
			exit 1
		fi
	elif [ "${microservice_selected}" == "4" ]; then
		echo "Change ${paas_git_repository}/logistics"
		if [ -d "${paas_git_repository}/logistics" ]; then
			cd ${paas_git_repository}/logistics
			git pull
			if [[ $? -eq 0 ]];then
				sh runDocker.sh
				whetherUpload
				echo "Change /home/repository/images"
				if [ -d "/home/repository/images" ]; then
						cd /home/repository/images
				else
						echo "No directory,Create it"
						mkdir -p /home/repository/images
						cd /home/repository/images
				fi
				echo "Export image"     
				docker save -o paas-logistics.tar paas-logistics
				echo "Upload image to remote server"
				read -p "Please enter remote ip:" ip
				scp /home/repository/images/paas-logistics.tar ${ip}:/home/repository/images

				if [[ $? -eq 0 ]];then
						echo "Change machine"
						ssh ${ip}
				else
						"Remote server /home/repository/images not found,Please create it first"
				fi
			else
				echo -e "Error:Uninstall GIT!"
			fi
		else
			echo -e "${red}Error:${plain} ${1} directory not found."
			exit 1
		fi
        elif [ "${microservice_selected}" == "5" ]; then
			echo "Change ${paas_git_repository}/sms"
			if [ -d "${paas_git_repository}/sms" ]; then
				cd ${paas_git_repository}/sms
				git pull
				if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o paas-sms.tar paas-sms
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/paas-sms.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
				else
					echo -e "Error:Uninstall GIT!"
				fi


			else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
			fi

        elif [ "${microservice_selected}" == "6" ]; then
			echo "Change ${paas_git_repository}/email"
			if [ -d "${paas_git_repository}/email" ]; then
				cd ${paas_git_repository}/email
				git pull
				if [[ $? -eq 0 ]];then
						sh runDocker.sh
						whetherUpload
						echo "Change /home/repository/images"
						if [ -d "/home/repository/images" ]; then
								cd /home/repository/images
						else
								echo "No directory,Create it"
								mkdir -p /home/repository/images
								cd /home/repository/images
						fi
						echo "Export image"     
						docker save -o paas-email.tar paas-email
						echo "Upload image to remote server"
						read -p "Please enter remote ip:" ip
						scp /home/repository/images/paas-email.tar ${ip}:/home/repository/images

						if [[ $? -eq 0 ]];then
								echo "Change machine"
								ssh ${ip}
						else
								"Remote server /home/repository/images not found,Please create it first"
						fi
				else
						echo -e "Error:Uninstall GIT!"
				fi


			else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
			fi

        elif [ "${microservice_selected}" == "7" ]; then
			echo "Change ${paas_git_repository}/dictionary"
			if [ -d "${paas_git_repository}/dictionary" ]; then
				cd ${paas_git_repository}/dictionary
				git pull
				if [[ $? -eq 0 ]];then
						sh runDocker.sh
						whetherUpload
						echo "Change /home/repository/images"
						if [ -d "/home/repository/images" ]; then
								cd /home/repository/images
						else
								echo "No directory,Create it"
								mkdir -p /home/repository/images
								cd /home/repository/images
						fi
						echo "Export image"     
						docker save -o paas-dic.tar paas-dic
						echo "Upload image to remote server"
						read -p "Please enter remote ip:" ip
						scp /home/repository/images/paas-dic.tar ${ip}:/home/repository/images

						if [[ $? -eq 0 ]];then
								echo "Change machine"
								ssh ${ip}
						else
								"Remote server /home/repository/images not found,Please create it first"
						fi
				else
					echo -e "Error:Uninstall GIT!"
				fi
			else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
			fi
        elif [ "${microservice_selected}" == "8" ]; then
			echo "Developing"
			exit 
			echo "Change ${paas_git_repository}"
			
			if [ -d "${paas_git_repository}" ]; then
				cd ${paas_git_repository}
				git pull
				if [[ $? -eq 0 ]];then
					startAllPaasServer
					whetherUpload
					
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o paas-dic.tar paas-dic
					docker save -o paas-email.tar paas-email
					docker save -o paas-goods.tar paas-goods
					docker save -o paas-inventory.tar paas-inventory
					docker save -o paas-sms.tar paas-sms
					docker save -o paas-user.tar paas-user
					docker save -o paas-logistics.tar paas-logistics

					echo "Upload images to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/paas-*.tar ${ip}:/home/repository/images
				else
					echo -e "Error:Uninstall GIT!"
				fi
			else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
			fi

	fi
}

startAllPaasServer() {
	echo
	echo "Change ${paas_git_repository}/dictionary"
	if [ -d "${paas_git_repository}/dictionary" ]; then 
		cd ${paas_git_repository}/dictionary
		sh runDocker.sh		
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${paas_git_repository}/email"
	if [ -d "${paas_git_repository}/email" ]; then 
		cd ${paas_git_repository}/email
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${paas_git_repository}/goods"
	if [ -d "${paas_git_repository}/goods" ]; then 
		cd ${paas_git_repository}/goods
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${paas_git_repository}/sms"
	if [ -d "${paas_git_repository}/sms" ]; then 
		cd ${paas_git_repository}/sms
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${paas_git_repository}/user"
	if [ -d "${paas_git_repository}/user" ]; then 
		cd ${paas_git_repository}/user
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${paas_git_repository}/inventory"
	if [ -d "${paas_git_repository}/inventory" ]; then 
		cd ${paas_git_repository}/inventory
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${paas_git_repository}/logistics"
	if [ -d "${paas_git_repository}/logistics" ]; then 
		cd ${paas_git_repository}/logistics
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi

}



config_mall_microservice() {
	if [[ "${microservice_selected}" == "1" ]]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/goods"
		if [ -d "${mall_git_repository}/goods" ]; then
			cd ${mall_git_repository}/goods
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-goods.tar mall-goods
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-goods.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
		
	elif [ "${microservice_selected}" == "2" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/cart"
		if [ -d "${mall_git_repository}/cart" ]; then
			cd ${mall_git_repository}/cart
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-cart.tar mall-cart
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-cart.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
		
	elif [ "${microservice_selected}" == "3" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/cms"
		if [ -d "${mall_git_repository}/cms" ]; then
			cd ${mall_git_repository}/cms
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-cms.tar mall-cms
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-cms.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
		
	elif [ "${microservice_selected}" == "4" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/evaluation"
		if [ -d "${mall_git_repository}/evaluation" ]; then
			cd ${mall_git_repository}/evaluation
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-evaluation.tar mall-evaluation
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-evaluation.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
		
	elif [ "${microservice_selected}" == "5" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/fileUpload"
		if [ -d "${mall_git_repository}/fileUpload" ]; then
			cd ${mall_git_repository}/fileUpload
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-fileupload.tar mall-fileupload
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-fileupload.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
		
	elif [ "${microservice_selected}" == "6" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/inventory"
		if [ -d "${mall_git_repository}/inventory" ]; then
			cd ${mall_git_repository}/inventory
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-inventory.tar mall-inventory
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-inventory.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
	elif [ "${microservice_selected}" == "7" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/logistics"
		if [ -d "${mall_git_repository}/logistics" ]; then
			cd ${mall_git_repository}/logistics
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-logistics.tar mall-logistics
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-logistics.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
	elif [ "${microservice_selected}" == "8" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/pay"
		if [ -d "${mall_git_repository}/pay" ]; then
			cd ${mall_git_repository}/pay
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-pay.tar mall-pay
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-pay.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
	
	elif [ "${microservice_selected}" == "9" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/push"
		if [ -d "${mall_git_repository}/push" ]; then
			cd ${mall_git_repository}/push
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-push.tar mall-push
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-push.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
	
	elif [ "${microservice_selected}" == "10" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/sso"
		if [ -d "${mall_git_repository}/sso" ]; then
			cd ${mall_git_repository}/sso
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-sso.tar mall-sso
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-sso.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
	
	elif [ "${microservice_selected}" == "11" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/userservice"
		if [ -d "${mall_git_repository}/userservice" ]; then
			cd ${mall_git_repository}/userservice
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-user.tar mall-user
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-user.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
		
	elif [ "${microservice_selected}" == "12" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/orders"
		if [ -d "${mall_git_repository}/orders" ]; then
			cd ${mall_git_repository}/orders
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-orders.tar mall-orders
					echo "Upload image to remote server"
					read -p "Please enter remote ip(much ip please segmentation by ';'):" ip
					scp /home/repository/images/mall-orders.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
	
	elif [ "${microservice_selected}" == "13" ]; then
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}/activity"
		if [ -d "${mall_git_repository}/activity" ]; then
			cd ${mall_git_repository}/activity
			git pull
			if [[ $? -eq 0 ]];then
					sh runDocker.sh
					whetherUpload
					echo "Change /home/repository/images"
					if [ -d "/home/repository/images" ]; then
							cd /home/repository/images
					else
							echo "No directory,Create it"
							mkdir -p /home/repository/images
							cd /home/repository/images
					fi
					echo "Export image"     
					docker save -o mall-activity.tar mall-activity
					echo "Upload image to remote server"
					read -p "Please enter remote ip:" ip
					scp /home/repository/images/mall-activity.tar ${ip}:/home/repository/images

					if [[ $? -eq 0 ]];then
							echo "Change machine"
							ssh ${ip}
					else
							"Remote server /home/repository/images not found,Please create it first"
					fi
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
	
	elif [ "${microservice_selected}" == "14" ]; then
		echo "Developing"
		exit
		echo "microservice = ${ymw_microservice}"
		echo "Change ${mall_git_repository}"
		if [ -d "${mall_git_repository}" ]; then
			cd ${mall_git_repository}
			git pull
			if [[ $? -eq 0 ]];then
				startAllMallServer
				whetherUpload
				
				echo "Change /home/repository/images"
				if [ -d "/home/repository/images" ]; then
						cd /home/repository/images
				else
						echo "No directory,Create it"
						mkdir -p /home/repository/images
						cd /home/repository/images
				fi
				echo "Export images"     
				docker save -o mall-activity.tar mall-activity
				docker save -o mall-cart.tar mall-cart
				docker save -o mall-cms.tar mall-cms
				docker save -o mall-evaluation.tar mall-evaluation
				docker save -o mall-fileUpload.tar mall-fileUpload
				docker save -o mall-goods.tar mall-goods
				docker save -o mall-inventory.tar mall-inventory
				docker save -o mall-logistics.tar mall-logistics
				docker save -o mall-orders.tar mall-orders
				docker save -o mall-pay.tar mall-pay
				docker save -o mall-push.tar mall-push
				docker save -o mall-sso.tar mall-sso
				docker save -o mall-userservice.tar mall-userservice

				echo "Upload images to remote server"
				read -p "Please enter remote ip:" ip
				scp /home/repository/images/mall-*.tar ${ip}:/home/repository/images
			else
					echo -e "Error:Uninstall GIT!"
			fi
		else
				echo -e "${red}Error:${plain} ${1} directory not found."
				exit 1
		fi
	fi
}

startAllMallServer() {
	echo
	echo "Change ${mall_git_repository}/activity"
	if [ -d "${mall_git_repository}/activity" ]; then 
		cd ${mall_git_repository}/activity
		sh runDocker.sh		
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${mall_git_repository}/cart"
	if [ -d "${mall_git_repository}/cart" ]; then 
		cd ${mall_git_repository}/cart
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${mall_git_repository}/cms"
	if [ -d "${mall_git_repository}/cms" ]; then 
		cd ${mall_git_repository}/cms
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${mall_git_repository}/evaluation"
	if [ -d "${mall_git_repository}/evaluation" ]; then 
		cd ${mall_git_repository}/evaluation
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${mall_git_repository}/fileUpload"
	if [ -d "${mall_git_repository}/fileUpload" ]; then 
		cd ${mall_git_repository}/fileUpload
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${mall_git_repository}/goods"
	if [ -d "${mall_git_repository}/goods" ]; then 
		cd ${mall_git_repository}/goods
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${mall_git_repository}/inventory"
	if [ -d "${mall_git_repository}/inventory" ]; then 
		cd ${mall_git_repository}/inventory
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	
	echo
	echo "Change ${mall_git_repository}/logistics"
	if [ -d "${mall_git_repository}/logistics" ]; then 
		cd ${mall_git_repository}/logistics
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${mall_git_repository}/orders"
	if [ -d "${mall_git_repository}/orders" ]; then 
		cd ${mall_git_repository}/orders
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${mall_git_repository}/pay"
	if [ -d "${mall_git_repository}/pay" ]; then 
		cd ${mall_git_repository}/pay
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${mall_git_repository}/push"
	if [ -d "${mall_git_repository}/push" ]; then 
		cd ${mall_git_repository}/push
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${mall_git_repository}/sso"
	if [ -d "${mall_git_repository}/sso" ]; then 
		cd ${mall_git_repository}/sso
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi
	
	echo
	echo "Change ${mall_git_repository}/userservice"
	if [ -d "${mall_git_repository}/userservice" ]; then 
		cd ${mall_git_repository}/userservice
		sh runDocker.sh	
	else
		echo -e "${red}Error:${plain} ${1} directory not found."
	fi


}

action=$1
[ -z $1 ] && action=deploy
case "$action" in
deploy | undeploy)
        ${action}_microservice
        ;;
*)
        echo "Arguments error! [${action}]"
        echo "Usage: $(basename $0) [deploy|undeploy]"
        ;;
esac

