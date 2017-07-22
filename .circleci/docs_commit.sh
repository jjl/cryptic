set -e
git config user.name "CircleCI"
git config user.email "docs@circleci"
git add -A .
git commit -m "Rebuild website documentation"
mkdir -p $HOME/.ssh
chmod 700 ~/.ssh
chmod 644 ~/.ssh/known_hosts
cat ~/.ssh/known_hosts
sflsfldsjflsj
#git push -f origin gh-pages
