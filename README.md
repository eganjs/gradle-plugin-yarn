# Yarn Plugin

## Mission Statement

This is intended to be an opinionated zero configuration plugin.

The idea is to provide a plugin that just orchestrates and keeps out of your way as much as possible.

The following scripts should be defined in your `package.json`:
- `clean` - links to `gradle clean`
- `build` - links to `gradle assemble`
- `lint` - links to `gradle check`  
- `test` - links to `gradle check`  
