(menu-bar-mode -1)
(tool-bar-mode -1)

(global-auto-revert-mode)

(setq frame-title-format
     '("%S" (buffer-file-name "%f"
                     (dired-directory dired-directory "%b"))))

(require 'cc-mode)  
(c-set-offset 'inline-open 0)  
(c-set-offset 'friend '-)  
(c-set-offset 'substatement-open 0)  


(setq-default ispell-program-name "aspell")
(add-hook 'text-mode-hook 'flyspell-mode)

(setq scroll-margin 10
      scroll-conservatively 10000)

(global-font-lock-mode t)

(show-paren-mode t)

(setq show-paren-style 'parentheses)

;;cscope  
(load "~/.emacs.d/xcscope.el")

(global-set-key [f11] 'delete-other-windows)

(global-set-key (kbd "M-]") 'cscope-find-this-symbol)
(global-set-key (kbd "M-k") 'cscope-pop-mark)
(global-set-key (kbd "M-p") 'cscope-prev-symbol)
(global-set-key (kbd "M-n") 'cscope-next-symbol)

;;(require 'xcscope)

(setq cscope-do-not-update-database t)  

(load "desktop") 
(desktop-load-default) 

(desktop-read)

(global-set-key "\C-xg" 'goto-line)

;; Set the tab width
(setq indent-tabs-mode t)
(setq-default indent-tabs-mode t)
(column-number-mode t)
(line-number-mode t)

(setq
    backup-by-copying t      ; don't clobber symlinks
    backup-directory-alist
    '(("." . "~/.saves"))    ; don't litter my fs tree
    delete-old-versions t
    kept-new-versions 6
    kept-old-versions 2
    version-control t)       ; use versioned backups

; (global-linum-mode t)

(global-set-key (kbd "C-?") 'help-command)
(global-set-key (kbd "M-?") 'mark-paragraph)
(global-set-key (kbd "C-h") 'delete-backward-char)
(global-set-key (kbd "M-h") 'backward-kill-word)

;; Turn on tabs
;;
(setq-default indent-tabs-mode nil)
(setq default-tab-width 2)
(setq tab-width 2)
(setq c-basic-indent 2)
(setq c-basic-offset 2)

