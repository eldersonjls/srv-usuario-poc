#!/usr/bin/env python3
import os
import re
import sys

def remove_lombok_from_file(filepath):
    """Remove Lombok annotations and imports from a Java file."""
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # Remove Lombok import statements
        content = re.sub(r'import lombok\.\*;\n', '', content)
        content = re.sub(r'import lombok\.[a-zA-Z.]+;\n', '', content)
        
        # Remove Lombok annotations (one or more @annotation\n patterns)
        content = re.sub(r'@Slf4j\n', '', content)
        content = re.sub(r'@RequiredArgsConstructor\n', '', content)
        content = re.sub(r'@Data\n', '', content)
        content = re.sub(r'@NoArgsConstructor\n', '', content)
        content = re.sub(r'@AllArgsConstructor\n', '', content)
        content = re.sub(r'@Builder\n', '', content)
        content = re.sub(r'@EqualsAndHashCode\([^)]*\)\n', '', content)
        content = re.sub(r'@Builder\.Default\n', '', content)
        
        if content != original_content:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"✓ {filepath}")
            return True
        return False
    except Exception as e:
        print(f"✗ {filepath}: {e}")
        return False

# Find all Java files
java_files = []
for root, dirs, files in os.walk('/workspaces/srv-usuario-poc/src'):
    for file in files:
        if file.endswith('.java'):
            java_files.append(os.path.join(root, file))

print(f"Found {len(java_files)} Java files")
updated = 0
for java_file in java_files:
    if remove_lombok_from_file(java_file):
        updated += 1

print(f"\nUpdated {updated} files")
