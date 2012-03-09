#!/bin/bash

#
# Script to add autocompletion to ant targets in bash
#

function ant_list() {
   local cur prev opts projects

   COMPREPLY=()
   cur="${COMP_WORDS[COMP_CWORD]}"
   prev="${COMP_WORDS[COMP_CWORD-1]}"

   case "${prev}" in
        "-lib" | "-buildfile" | "-file" | "-f" | "-propertyfile" | "-find" | "-s")
              COMPREPLY=( $(compgen -f ${cur}) )
              return 0
              ;;
        *)
              ;;

   esac
 
   if [[ ${cur} == -* ]] ; then
        opts="-help -projecthelp -version -diagnostics -quiet -verbose -debug -emacs -lib -logfile -logger -listener -noinput -buildfile -file -D -keep-going -propertyfile -inputhandler -find -nice -nouserlib -noclasspath -autoproxy -main"
        COMPREPLY=( $(compgen -W "${opts}" -- ${cur}) )
   return 0
   else
        projects=`ant -projecthelp | perl -n -e 'if(/^\s(\S+)/) { print $1, " "; }'`
        COMPREPLY=( $(compgen -W "${projects}" -- ${cur}))
        return 0
   fi
}

complete -F ant_list ant
