git config user.name "CircleCI"
git config user.email "docs@circleci"
git add -A .
git commit -m "Rebuild website documentation"
cat ~/.ssh/known_hosts
git remote add docs git@github.com:irresponsible-docs/cryptic
git push -f docs gh-pages
