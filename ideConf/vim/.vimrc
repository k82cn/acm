syntax on

filetype plugin on

" set cscopequickfix=s-,c-,d-,i-,t-,e-
set cscopequickfix=c-,d-,e-,g-,i-,s-,t-

" un-comment the follow setting to show new line, tab
set list
"
set expandtab
set tabstop=4
set shiftwidth=4
set autoindent

set cindent
set showmatch
set so=10
set ruler
set nu

colorscheme desert
" colorscheme elflord

let Tlist_Show_One_File=1
let Tlist_Exit_OnlyWindow=1

let g:winManagerWindowLayout='FileExplorer|TagList'

nmap <c-l> :only<cr>

map <c-w><c-t> :WMToggle<cr> 

set autowrite

